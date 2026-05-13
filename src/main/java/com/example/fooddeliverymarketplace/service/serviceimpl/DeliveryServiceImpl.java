package com.example.fooddeliverymarketplace.service.serviceimpl;

import com.example.fooddeliverymarketplace.entity.Delivery;
import com.example.fooddeliverymarketplace.entity.User;
import com.example.fooddeliverymarketplace.entity.order.Order;
import com.example.fooddeliverymarketplace.entity.order.OrderItem;
import com.example.fooddeliverymarketplace.exception.DeliveryNotFoundException;
import com.example.fooddeliverymarketplace.exception.OrderNotFoundException;
import com.example.fooddeliverymarketplace.exception.user.UserNotFoundException;
import com.example.fooddeliverymarketplace.mapper.DeliveryMapper;
import com.example.fooddeliverymarketplace.mapper.OrderItemMapper;
import com.example.fooddeliverymarketplace.mapper.OrderMapper;
import com.example.fooddeliverymarketplace.payload.delivery.DeliveryResponseWithOrder;
import com.example.fooddeliverymarketplace.payload.order.OrderItemResponse;
import com.example.fooddeliverymarketplace.repository.DeliveryRepository;
import com.example.fooddeliverymarketplace.repository.UserRepository;
import com.example.fooddeliverymarketplace.repository.order.OrderItemRepository;
import com.example.fooddeliverymarketplace.repository.order.OrderRepository;
import com.example.fooddeliverymarketplace.service.DeliveryService;
import com.example.fooddeliverymarketplace.utils.DeliveryStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Override
    @Cacheable(value = "availableDeliveries")
    public List<DeliveryResponseWithOrder> getAvailableDeliveries() {
        List<Delivery> deliveriesByStatus = deliveryRepository.findDeliveriesByStatus(DeliveryStatus.WAITING_FOR_COURIER);

        Order order = deliveriesByStatus.getFirst().getOrder();

        List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(order.getId());

        List<OrderItemResponse> orderItemResponses = orderItems.stream().map(orderItemMapper::toOrderItemResponse).toList();

        return deliveriesByStatus.stream()
                .map(del -> deliveryMapper.toDeliveryResponseWithOrder(del.getStatus(), del.getDeliveryAddress(), orderMapper.toOrderResponse(order, orderItemResponses)))
                .toList();
    }

    @Override
    @Transactional
    public void acceptDelivery(Long deliveryId, Authentication authentication) {
        User courier = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("Courier not found"));

        Delivery delivery = deliveryRepository.findByIdWithLock(deliveryId)
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery not found"));

        if (delivery.getStatus() != DeliveryStatus.WAITING_FOR_COURIER) {
            throw new IllegalStateException("Delivery already assigned to another courier");
        }

        delivery.setCourier(courier);
        delivery.setStatus(DeliveryStatus.COURIER_ASSIGNED);

        deliveryRepository.save(delivery);
    }

    @Override
    @Transactional
    public void pickUpDelivery(Long deliveryId, Authentication authentication) {
        Delivery delivery = deliveryRepository.findByIdWithLock(deliveryId)
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery not found"));

        if (delivery.getStatus() != DeliveryStatus.COURIER_ASSIGNED) {
            throw new IllegalStateException("Delivery already picked up");
        }

        delivery.setStatus(DeliveryStatus.PICKED_UP);
        delivery.setEstimatedDeliveryTime(LocalDateTime.now());

        deliveryRepository.save(delivery);
    }

    @Override
    @Transactional
    public void completeDelivery(Long deliveryId, Authentication authentication) {
        Delivery delivery = deliveryRepository.findByIdWithLock(deliveryId)
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery not found"));

        if (delivery.getStatus() != DeliveryStatus.ON_THE_WAY) {
            throw new IllegalStateException("Delivery already taken");
        }

        delivery.setStatus(DeliveryStatus.DELIVERED);
        delivery.setDeliveredAt(LocalDateTime.now());

        deliveryRepository.save(delivery);
    }

    @Override
    @Transactional
    public void onTheWayDelivery(Long deliveryId, Authentication authentication) {
        Delivery delivery = deliveryRepository.findByIdWithLock(deliveryId)
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery not found"));

        if (delivery.getStatus() != DeliveryStatus.PICKED_UP) {
            throw new IllegalStateException("Delivery already taken");
        }

        delivery.setStatus(DeliveryStatus.ON_THE_WAY);
        delivery.setEstimatedDeliveryTime(LocalDateTime.now());

        deliveryRepository.save(delivery);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryResponseWithOrder> getUserDeliveries(Long orderId, Authentication authentication) {
        List<Delivery> deliveriesByOrderId = deliveryRepository.findDeliveriesByOrderId(orderId);

        if (deliveriesByOrderId.isEmpty()) {
            throw new DeliveryNotFoundException("No delivery information found for order ID: " + orderId);
        }

        List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(orderId);

        List<OrderItemResponse> orderItemResponses = orderItems.stream().map(orderItemMapper::toOrderItemResponse).toList();

        Order order = orderRepository.findByOrderId(orderId).orElseThrow(() -> new OrderNotFoundException("order not found"));

        return deliveriesByOrderId.stream()
                .map(delivery -> deliveryMapper.toDeliveryResponseWithOrder(
                        delivery.getStatus(),
                        delivery.getDeliveryAddress(),
                        orderMapper.toOrderResponse(order, orderItemResponses)
                ))
                .toList();
    }

    @CacheEvict(cacheNames = {"restaurants","restaurant-menu"}, allEntries = true)
    @Scheduled(cron = "* */2 * * * *")
    public void evictCache() {
        log.info("restaurant related cache evict");
    }
}
