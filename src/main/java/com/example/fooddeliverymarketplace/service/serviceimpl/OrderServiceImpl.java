package com.example.fooddeliverymarketplace.service.serviceimpl;

import com.example.fooddeliverymarketplace.entity.Restaurant;
import com.example.fooddeliverymarketplace.entity.User;
import com.example.fooddeliverymarketplace.entity.cart.Cart;
import com.example.fooddeliverymarketplace.entity.cart.CartItem;
import com.example.fooddeliverymarketplace.entity.order.Order;
import com.example.fooddeliverymarketplace.entity.order.OrderItem;
import com.example.fooddeliverymarketplace.exception.OrderNotFoundException;
import com.example.fooddeliverymarketplace.exception.cart.CartEmptyExceprion;
import com.example.fooddeliverymarketplace.exception.cart.CartNotFoundException;
import com.example.fooddeliverymarketplace.mapper.OrderItemMapper;
import com.example.fooddeliverymarketplace.mapper.OrderMapper;
import com.example.fooddeliverymarketplace.payload.order.OrderItemResponse;
import com.example.fooddeliverymarketplace.payload.order.OrderRequest;
import com.example.fooddeliverymarketplace.payload.order.OrderResponse;
import com.example.fooddeliverymarketplace.payload.order.UpdateOrderStatusRequest;
import com.example.fooddeliverymarketplace.repository.UserRepository;
import com.example.fooddeliverymarketplace.repository.cartrepository.CartItemRepository;
import com.example.fooddeliverymarketplace.repository.cartrepository.CartRepository;
import com.example.fooddeliverymarketplace.repository.order.OrderItemRepository;
import com.example.fooddeliverymarketplace.repository.order.OrderRepository;
import com.example.fooddeliverymarketplace.service.OrderService;
import com.example.fooddeliverymarketplace.service.OrderTrackingService;
import com.example.fooddeliverymarketplace.utils.OrderStatus;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderTrackingService orderTrackingService;

    @Override
    @Transactional
    public OrderResponse create(OrderRequest orderRequest, Authentication authentication) {
        User user = getUser(authentication);

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new CartNotFoundException(
                                "Cart not found"
                        )
                );

        List<CartItem> cartItems =
                cartItemRepository.findByCartId(cart.getId());

        if (cartItems.isEmpty()) {
            throw new CartEmptyExceprion("Cart is empty");
        }

        Restaurant restaurant =
                cartItems.getFirst()
                        .getMenuItem()
                        .getRestaurant();

        Order order = Order.builder()
                .user(user)
                .restaurant(restaurant)
                .status(OrderStatus.PENDING)
                .totalPrice(cart.getTotalPrice())
                .createdAt(LocalDateTime.now())
                .build();

        orderRepository.save(order);

        orderTrackingService.addEvent(
        order.getId(),
        OrderStatus.PENDING,
        "Order was created and is waiting for restaurant confirmation",
        java.util.Map.of(
                "restaurantId", restaurant.getId(),
                "userId", user.getId()
            )
        );

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .menuItem(cartItem.getMenuItem())
                    .quantity(cartItem.getQuantity())
                    .price(cartItem.getPrice())
                    .build();

            orderItems.add(orderItem);
        }

        List<OrderItemResponse> orderItemResponse = orderItems.stream().map(orderItemMapper::toOrderItemResponse).toList();

        orderItemRepository.saveAll(orderItems);

        cartItemRepository.deleteAll(cartItems);

        cart.setTotalPrice(BigDecimal.ZERO);

        cartRepository.save(cart);

        return orderMapper.toOrderResponse(order, orderItemResponse);
    }

    @Override
    @Cacheable(value = "orders", key = "#authentication.name")
    public List<OrderResponse> getMyOrders(Authentication authentication) {
        User user = getUser(authentication);

        List<Order> orders = orderRepository.findAllByUserId(user.getId());

        if (orders.isEmpty()) {
            return Collections.emptyList();
        }

        return orders.stream()
                .map(order -> {
                    List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(order.getId());
                    List<OrderItemResponse> orderItemResponses = orderItems.stream().map(orderItemMapper::toOrderItemResponse).toList();
                    return orderMapper.toOrderResponse(order, orderItemResponses);
                }).toList();
    }

    @Override
    @Cacheable(value = "orders", key = "#orderId")
    public OrderResponse getOrderById(Long orderId, Authentication authentication) {
        User user = getUser(authentication);

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("order with id: " + orderId + " not found"));

        List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(order.getId());
        List<OrderItemResponse> orderItemResponses = orderItems.stream().map(orderItemMapper::toOrderItemResponse).toList();
        return orderMapper.toOrderResponse(order, orderItemResponses);
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, UpdateOrderStatusRequest orderStatus, Authentication authentication) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("order with id: " + orderId + " not found"));

        order.setStatus(orderStatus.orderStatus());
        orderRepository.save(order);

        orderTrackingService.addEvent(
        order.getId(),
        orderStatus.orderStatus(),
        "Order status was updated to " + orderStatus.orderStatus(),
        java.util.Map.of(
                "restaurantId", order.getRestaurant().getId(),
                "userId", order.getUser().getId()
            )
        );
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId, Authentication authentication) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("order with id: " + orderId + " not found"));

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        orderTrackingService.addEvent(
        order.getId(),
        OrderStatus.CANCELLED,
        "Order was cancelled",
        java.util.Map.of(
                "restaurantId", order.getRestaurant().getId(),
                "userId", order.getUser().getId()
            )
        );
    }

    private User getUser(Authentication authentication) {
        String email = authentication.getName();

        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("username not found"));
    }

    @CacheEvict(cacheNames = {"orders"}, allEntries = true)
    @Scheduled(cron = "0 0 * * * *")
    public void evictCache() {
        log.info("restaurant related cache evict");
    }
}
