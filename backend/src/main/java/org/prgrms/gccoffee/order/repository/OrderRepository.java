package org.prgrms.gccoffee.order.repository;

import org.prgrms.gccoffee.order.model.Email;
import org.prgrms.gccoffee.order.model.Order;
import org.prgrms.gccoffee.order.model.OrderItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    List<Order> findAll();

    List<Order> findByEmail(Email email);

    List<OrderItem> findOrderItem(UUID orderId);

    Optional<Order> findById(UUID orderId);

    Order insert(Order order);
}
