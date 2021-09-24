package org.prgrms.gccoffee.order.model.dto;

import lombok.Getter;
import org.prgrms.gccoffee.order.model.OrderItem;

import java.util.List;

@Getter
public class CreateOrderRequest {
    private final String email;
    private final String address;
    private final String postcode;
    private final List<OrderItem> orderItems;

    public CreateOrderRequest(String email, String address, String postcode, List<OrderItem> orderItems) {
        this.email = email;
        this.address = address;
        this.postcode = postcode;
        this.orderItems = orderItems;
    }
}
