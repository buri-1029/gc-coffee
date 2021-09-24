package org.prgrms.gccoffee.order.controller;

import org.prgrms.gccoffee.order.model.Order;
import org.prgrms.gccoffee.order.model.OrderItem;
import org.prgrms.gccoffee.order.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@Controller
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public String ordersPage(Model model) {
        List<Order> orders = orderService.getOrders();
        model.addAttribute("orders", orders);
        return "order-list";
    }

    @GetMapping("/order-items/{orderId}")
    public String orderItemsPage(@PathVariable("orderId") String orderId, Model model) {
        List<OrderItem> orderItems = orderService.getOrderItems(UUID.fromString(orderId));
        model.addAttribute("orderItems", orderItems);
        return "order-items";
    }
}
