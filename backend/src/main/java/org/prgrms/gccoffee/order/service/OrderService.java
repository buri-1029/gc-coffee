package org.prgrms.gccoffee.order.service;

import org.prgrms.gccoffee.order.model.Email;
import org.prgrms.gccoffee.order.model.Order;
import org.prgrms.gccoffee.order.model.OrderItem;

import java.util.List;

public interface OrderService {
    List<Order> getOrdersByEmail(Email email);

    Order createOrder(Email email, String address, String postcode, List<OrderItem> orderItems);
}
