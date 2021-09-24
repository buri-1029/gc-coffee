package org.prgrms.gccoffee.order.controller;

import org.prgrms.gccoffee.order.model.Email;
import org.prgrms.gccoffee.order.model.Order;
import org.prgrms.gccoffee.order.model.dto.CreateOrderRequest;
import org.prgrms.gccoffee.order.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderRestController {

    private final OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/v1/orders")
    public Order createOrder(@RequestBody CreateOrderRequest orderRequest) {
        return orderService.createOrder(
                new Email(orderRequest.getEmail()),
                orderRequest.getAddress(),
                orderRequest.getPostcode(),
                orderRequest.getOrderItems()
        );
    }

    @GetMapping("/api/v1/orders")
    public List<Order> getOrder(@RequestParam("email") String email) {
        return orderService.getOrdersByEmail(new Email(email));
    }
}