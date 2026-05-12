package com.example.fooddeliverymarketplace.service.serviceimpl;

import com.example.fooddeliverymarketplace.entity.MenuItem;
import com.example.fooddeliverymarketplace.entity.Restaurant;
import com.example.fooddeliverymarketplace.entity.User;
import com.example.fooddeliverymarketplace.exception.restaurant.RestaurantNotFoundException;
import com.example.fooddeliverymarketplace.mapper.MenuItemMapper;
import com.example.fooddeliverymarketplace.mapper.RestaurantMapper;
import com.example.fooddeliverymarketplace.payload.restaurantpayload.RestaurantRequest;
import com.example.fooddeliverymarketplace.payload.restaurantpayload.RestaurantResponse;
import com.example.fooddeliverymarketplace.payload.restaurantpayload.RestaurantResponseWithMenuItems;
import com.example.fooddeliverymarketplace.repository.MenuItemRepository;
import com.example.fooddeliverymarketplace.repository.RestaurantRepository;
import com.example.fooddeliverymarketplace.repository.UserRepository;
import com.example.fooddeliverymarketplace.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;
    private final UserRepository userRepository;
    private final MenuItemMapper menuItemMapper;
    private final MenuItemRepository  menuItemRepository;

    @Override
    public List<RestaurantResponse> findAll() {
        List<Restaurant> all = restaurantRepository.findAll();

        if (all.isEmpty()) {
            throw new RestaurantNotFoundException("Restaurants not created yet");
        }

        return all
                .stream()
                .map(restaurantMapper::toRestaurantResponse)
                .toList();
    }

    @Override
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
        String name = authentication.getName();

        User user = userRepository.findByEmail(name).orElseThrow(() -> new UsernameNotFoundException("User with email: " + name + " not found"));

        List<RestaurantResponse> all = findAll();

        if (all.isEmpty()) {
            throw new RestaurantNotFoundException("Restaurants not created yet");
        }

        List<RestaurantResponse> ownerRestaurants = new ArrayList<>();

        for (RestaurantResponse restaurantResponse : all) {
            if (Objects.equals(restaurantResponse.ownerName(), user.getFullName())){
                ownerRestaurants.add(restaurantResponse);
            }
        }

        return ownerRestaurants;
    }

    @Override
    public List<RestaurantResponseWithMenuItems> getAllItemsByRestaurantId(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));

        List<MenuItem> all = menuItemRepository.findAll();

        List<RestaurantResponseWithMenuItems> allItems = new ArrayList<>();

        for (MenuItem menuItem : all) {
            if (menuItem.getRestaurant().getRestaurantName().equals(restaurant.getRestaurantName())){
                allItems.add(new RestaurantResponseWithMenuItems(
                        menuItemMapper.toMenuItemResponse(menuItem),
                        restaurant.getRestaurantName()
                ));
            }
        }

        return allItems;
    }
}
