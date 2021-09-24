package org.prgrms.gccoffee.order.service;

import org.prgrms.gccoffee.order.model.Email;
import org.prgrms.gccoffee.order.model.Order;
import org.prgrms.gccoffee.order.model.OrderItem;
import org.prgrms.gccoffee.order.model.OrderStatus;
import org.prgrms.gccoffee.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getOrdersByEmail(Email email) {
        return orderRepository.findByEmail(email);
    }

    @Override
    public List<OrderItem> getOrderItems(UUID orderId) {
        return orderRepository.findOrderItem(orderId);
    }

    @Override
    public Order createOrder(Email email, String address, String postcode, List<OrderItem> orderItems) {
        Order order = new Order(
                UUID.randomUUID(),
                email,
                address,
                postcode,
                orderItems,
                OrderStatus.ACCEPTED,
                LocalDateTime.now(),
                LocalDateTime.now());
        return orderRepository.insert(order);
    }
}
