package org.prgrms.gccoffee.order.service;

import org.prgrms.gccoffee.order.model.Email;
import org.prgrms.gccoffee.order.model.Order;
import org.prgrms.gccoffee.order.model.OrderItem;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<Order> getOrders();

    List<Order> getOrdersByEmail(Email email);

    List<OrderItem> getOrderItems(UUID orderId);

    Order createOrder(Email email, String address, String postcode, List<OrderItem> orderItems);
}
