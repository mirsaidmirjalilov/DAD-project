package com.example.fooddeliverymarketplace.service.serviceimpl;

import com.example.fooddeliverymarketplace.entity.MenuItem;
import com.example.fooddeliverymarketplace.entity.Restaurant;
import com.example.fooddeliverymarketplace.exception.menuitem.ItemInMenuNotFoundException;
import com.example.fooddeliverymarketplace.exception.restaurant.RestaurantNotFoundException;
import com.example.fooddeliverymarketplace.mapper.MenuItemMapper;
import com.example.fooddeliverymarketplace.payload.menuitempayload.MenuItemRequest;
import com.example.fooddeliverymarketplace.payload.menuitempayload.MenuItemResponse;
import com.example.fooddeliverymarketplace.repository.MenuItemRepository;
import com.example.fooddeliverymarketplace.repository.RestaurantRepository;
import com.example.fooddeliverymarketplace.service.MenuItemService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuItemServiceImpl implements MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final MenuItemMapper menuItemMapper;
    private final RestaurantRepository restaurantRepository;

    @Override
    @Cacheable(value = "menuItems", key = "#menuItemId")
    public MenuItemResponse getByMenuItemId(Long menuItemId) {
        MenuItem foundInMenu = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ItemInMenuNotFoundException("Item not found in Menu"));

        return menuItemMapper.toMenuItemResponse(foundInMenu);
    }

    @Override
    @Transactional
    public MenuItemResponse createMenuItem(MenuItemRequest menuItemRequest, Authentication authentication) {
        checkIsAdmin(authentication);

        Restaurant restaurant = restaurantRepository.findById(menuItemRequest.restaurantId()).orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));

        MenuItem menuItem = MenuItem.builder()
                .available(true)
                .name(menuItemRequest.name())
                .description(menuItemRequest.description())
                .price(menuItemRequest.price())
                .restaurant(restaurant)
                .build();
        menuItemRepository.save(menuItem);
        return menuItemMapper.toMenuItemResponse(menuItem);
    }

    @Override
    @Transactional
    public MenuItemResponse update(
            Long menuItemId,
            MenuItemRequest menuItemRequest,
            Authentication authentication
    ) {
        checkIsAdmin(authentication);

        MenuItem menuItem = menuItemRepository.findById(menuItemId).orElseThrow(() -> new ItemInMenuNotFoundException("Item not found in Menu"));
        Restaurant restaurant = restaurantRepository.findById(menuItemRequest.restaurantId()).orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));

        menuItem.setRestaurant(restaurant);
        menuItem.setDescription(menuItemRequest.description());
        menuItem.setPrice(menuItemRequest.price());
        menuItem.setName(menuItemRequest.name());

        menuItemRepository.save(menuItem);
        return menuItemMapper.toMenuItemResponse(menuItem);
    }

    @Override
    @Transactional
    public void delete(Long menuItemId, Authentication authentication) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId).orElseThrow(() -> new ItemInMenuNotFoundException("Item not found in Menu"));

        menuItem.setAvailable(false);
        menuItemRepository.save(menuItem);
    }

    private void checkIsAdmin(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new AccessDeniedException("you are not admin");
        }
    }

    @CacheEvict(cacheNames = {"menuItems"}, allEntries = true)
    @Scheduled(cron = "0 0 * * * *")
    public void evictCache() {
        log.info("restaurant related cache evict");
    }
}
