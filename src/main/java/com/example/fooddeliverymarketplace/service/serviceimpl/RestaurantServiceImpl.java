package com.example.fooddeliverymarketplace.service.serviceimpl;

import com.example.fooddeliverymarketplace.entity.MenuItem;
import com.example.fooddeliverymarketplace.entity.Restaurant;
import com.example.fooddeliverymarketplace.entity.User;
import com.example.fooddeliverymarketplace.exception.menuitem.ItemInMenuNotFoundException;
import com.example.fooddeliverymarketplace.exception.restaurant.RestaurantNotFoundException;
import com.example.fooddeliverymarketplace.mapper.MenuItemMapper;
import com.example.fooddeliverymarketplace.mapper.RestaurantMapper;
import com.example.fooddeliverymarketplace.payload.restaurantpayload.RestaurantRequest;
import com.example.fooddeliverymarketplace.payload.restaurantpayload.RestaurantResponse;
import com.example.fooddeliverymarketplace.payload.restaurantpayload.RestaurantResponseWithMenuItems;
import com.example.fooddeliverymarketplace.repository.MenuItemRepository;
import com.example.fooddeliverymarketplace.repository.RestaurantRepository;
import com.example.fooddeliverymarketplace.repository.UserRepository;
import com.example.fooddeliverymarketplace.service.RedisService;
import com.example.fooddeliverymarketplace.service.RestaurantService;
import com.example.fooddeliverymarketplace.service.specification.SpecificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;
    private final UserRepository userRepository;
    private final MenuItemMapper menuItemMapper;
    private final MenuItemRepository  menuItemRepository;
    private final SpecificationService  specificationService;
    private final RedisService redisService;

    private static final String RESTAURANT_GET_KEY = "RESTAURANT_GET_KEY";
    private static final Long RESTAURANT_GET_KEY_TTL = 2L;

    @Override
    @Cacheable(value = "restaurants",key = "#restaurantId")
    public RestaurantResponse findById(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));

        return restaurantMapper.toRestaurantResponse(restaurant);
    }

    @Override
    public RestaurantResponse create(RestaurantRequest restaurantRequest, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROEL_ADMIN"));

        if (!isAdmin) {
            throw new AccessDeniedException("You cannot edit this restaurant");
        }

        String email = authentication.getName();

        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + email + " not found"));

        Restaurant restaurant = Restaurant.builder()
                .restaurantName(restaurantRequest.restaurantName())
                .description(restaurantRequest.description())
                .address(restaurantRequest.address())
                .phoneNumber(restaurantRequest.phoneNumber())
                .owner(owner)
                .build();
        restaurantRepository.save(restaurant);
        return restaurantMapper.toRestaurantResponse(restaurant);
    }

    @Override
    public RestaurantResponse update(Long restaurantId, RestaurantRequest restaurantRequest, Authentication authentication) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant with id: " + restaurantId + " not found"));

        String email = authentication.getName();

        boolean isOwner = restaurant.getOwner().getEmail().equals(email);

        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROEL_ADMIN"));

        if (!isOwner || !isAdmin) {
            throw new AccessDeniedException("You cannot edit this restaurant");
        }

        restaurant.setDescription(restaurantRequest.description());
        restaurant.setAddress(restaurantRequest.address());
        restaurant.setPhoneNumber(restaurantRequest.phoneNumber());
        restaurant.setOwner(restaurant.getOwner());

        Restaurant save = restaurantRepository.save(restaurant);

        return restaurantMapper.toRestaurantResponse(save);
    }

    @Override
    public void delete(Long restaurantId, Authentication authentication) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant with id: " + restaurantId + " not found"));

        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROEL_ADMIN"));

        if (!isAdmin) {
            throw new AccessDeniedException("You cannot delete this restaurant");
        }

        restaurant.setActive(false);
        restaurantRepository.save(restaurant);
    }

    @Override
    public List<RestaurantResponse> findAllByOwner(Authentication authentication) {
        String email = authentication.getName();

        User owner = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with email: " + email + " not found"));

        List<Restaurant> all = restaurantRepository.findAllByOwnerName(owner.getFullName());

        if (all.isEmpty()) {
            throw new RestaurantNotFoundException("Restaurants not created yet");
        }

        return all.stream()
                .filter(restaurant -> restaurant.getOwner().getEmail().equals(email))
                .map(restaurantMapper::toRestaurantResponse)
                .toList();
    }

    @Override
    @Cacheable(value = "restaurant-menu", key = "#restaurantId")
    public List<RestaurantResponseWithMenuItems> getAllItemsByRestaurantId(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));

        List<MenuItem> all = menuItemRepository.findByRestaurantId(restaurant.getId());

        if (all.isEmpty()) {
            throw new ItemInMenuNotFoundException("Items not found");
        }

        return all.stream()
                .map(menuItem -> new RestaurantResponseWithMenuItems(
                        menuItemMapper.toMenuItemResponse(menuItem),
                        restaurant.getRestaurantName()
                ))
                .toList();
    }

    @Override
    @Cacheable("restaurants")
    public List<RestaurantResponse> findAllByCriterias(String restaurantName, Float rating, Boolean active, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<Restaurant> specification = specificationService.getRestarauntSpecification(restaurantName,rating,active);

        Page<Restaurant> restaurantPage = restaurantRepository.findAll(specification, pageable);

        return restaurantPage.stream()
                .map(restaurantMapper::toRestaurantResponse)
                .toList();
    }

    @CacheEvict(cacheNames = {"restaurants","restaurant-menu"}, allEntries = true)
    @Scheduled(cron = "* */5 * * * *")
    public void evictCache() {
        log.info("restaurant related cache evict");
    }
}
