package com.example.fooddeliverymarketplace.service.serviceimpl;

import com.example.fooddeliverymarketplace.entity.MenuItem;
import com.example.fooddeliverymarketplace.entity.User;
import com.example.fooddeliverymarketplace.entity.cart.Cart;
import com.example.fooddeliverymarketplace.entity.cart.CartItem;
import com.example.fooddeliverymarketplace.exception.cart.UserCartNotFoundException;
import com.example.fooddeliverymarketplace.exception.menuitem.ItemInMenuNotFoundException;
import com.example.fooddeliverymarketplace.mapper.CartMapper;
import com.example.fooddeliverymarketplace.payload.cart.CartRequest;
import com.example.fooddeliverymarketplace.payload.cart.CartResponse;
import com.example.fooddeliverymarketplace.repository.MenuItemRepository;
import com.example.fooddeliverymarketplace.repository.UserRepository;
import com.example.fooddeliverymarketplace.repository.cartrepository.CartItemRepository;
import com.example.fooddeliverymarketplace.repository.cartrepository.CartRepository;
import com.example.fooddeliverymarketplace.service.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;
    private final CartMapper cartMapper;

    @Override
    @Transactional
    public CartResponse create(CartRequest cartRequest, Authentication authentication) {
        User user = getUser(authentication);

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .user(user)
                            .totalPrice(BigDecimal.ZERO)
                            .build();
                    return cartRepository.save(newCart);
                });

        MenuItem menuItem = menuItemRepository
                .findById(cartRequest.menuItemId())
                .orElseThrow(() ->
                        new ItemInMenuNotFoundException(
                                "Menu item not found"
                        )
                );

        Optional<CartItem> optionalCartItem =
                cartItemRepository.findByCartIdAndMenuItemId(
                        cart.getId(),
                        menuItem.getId()
                );

        CartItem cartItem;

        if (optionalCartItem.isPresent()) {

            cartItem = optionalCartItem.get();

            cartItem.setQuantity(
                    cartItem.getQuantity() + cartRequest.quantity()
            );

            cartItem.setPrice(
                    BigDecimal.valueOf(menuItem.getPrice()).multiply(BigDecimal.valueOf(cartRequest.quantity())));
        } else {

            cartItem = CartItem.builder()
                    .cart(cart)
                    .menuItem(menuItem)
                    .quantity(cartRequest.quantity())
                    .price(BigDecimal.valueOf(menuItem.getPrice()).multiply(BigDecimal.valueOf(cartRequest.quantity())))
                    .build();
        }

        cartItemRepository.save(cartItem);

        BigDecimal totalPrice = cartItemRepository.findByCartId(cart.getId())
                .stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalPrice(totalPrice);
        cartRepository.save(cart);

        return cartMapper.toCartResponse(cart, menuItem.getRestaurant().getRestaurantName(), cartItemRepository.getAllByCartId(cart.getId()));
    }


    @Override
    @Cacheable(value = "userCarts", key = "#authentication.name")
    public CartResponse getUserCart(Authentication authentication) {
        User user = getUser(authentication);

        Cart byUserId = cartRepository.findByUserId(user.getId()).orElseThrow(() -> new UserCartNotFoundException("cart not found"));

        List<CartItem> cartItems = cartItemRepository.findByCartId(byUserId.getId());

        String restaurantName = null;
        if (!cartItems.isEmpty()) {
            restaurantName = cartItems.getFirst().getMenuItem().getRestaurant().getRestaurantName();
        }

        return cartMapper.toCartResponse(byUserId, restaurantName, cartItemRepository.getAllByCartId(byUserId.getId()));
    }

    @Override
    @Transactional
    public void clearCart(Authentication authentication) {
        User user = getUser(authentication);

        Cart byUserId = cartRepository.findByUserId(user.getId()).orElseThrow(() -> new UserCartNotFoundException("cart not found"));

        cartItemRepository.deleteAllByCartId(byUserId.getId());
    }

    @Override
    @Transactional
    public void deleteItemByCartId(Long cartItemId, Authentication authentication, Integer quantity) {
        if (quantity == null) {
            throw new IllegalArgumentException("Quantity to add must be greater than zero");
        }
        User user = getUser(authentication);

        Cart cart = cartRepository.findByUserId(user.getId()).orElseThrow(() -> new UserCartNotFoundException("cart not found"));

        CartItem item = cartItemRepository.findById(cartItemId)
                .filter(ci -> ci.getCart().getId().equals(cart.getId()))
                .orElseThrow(() -> new ItemInMenuNotFoundException("Item not found in your cart"));

        if (item.getQuantity() > quantity) {
            item.setQuantity(item.getQuantity() - quantity);
            cartItemRepository.save(item);
        } else {
            cartItemRepository.delete(item);
        }
    }

    @Override
    @Transactional
    public CartResponse addQuantityToCartItem(Authentication authentication, Long cartItemId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity to add must be greater than zero");
        }

        User user = getUser(authentication);

        Cart cart = cartRepository.findByUserId(user.getId()).orElseThrow(() -> new UserCartNotFoundException("cart not found"));

        CartItem item = cartItemRepository.findById(cartItemId)
                .filter(ci -> ci.getCart().getId().equals(cart.getId()))
                .orElseThrow(() -> new ItemInMenuNotFoundException("Item not found in your cart"));
        item.setQuantity(item.getQuantity() + quantity);
        cartItemRepository.save(item);

        List<CartItem> updatedItems = cartItemRepository.findByCartId(cart.getId());

        String restaurantName = item.getMenuItem().getRestaurant().getRestaurantName();

        return cartMapper.toCartResponse(cart, restaurantName, updatedItems);
    }

    private User getUser(Authentication authentication) {
        String email = authentication.getName();

        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("username not found"));
    }

    @CacheEvict(cacheNames = {"userCarts"}, allEntries = true)
    @Scheduled(cron = "0 0 * * * *")
    public void evictCache() {
        log.info("cart related cache evict");
    }
}
