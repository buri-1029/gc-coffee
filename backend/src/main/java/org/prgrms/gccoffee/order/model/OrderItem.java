package org.prgrms.gccoffee.order.model;

import lombok.Getter;
import org.prgrms.gccoffee.product.model.Category;

import java.util.UUID;

@Getter
public class OrderItem {
    private final UUID productId;
    private final Category category;
    private final long price;
    private final int quantity;

    public OrderItem(UUID productId, Category category, long price, int quantity) {
        this.productId = productId;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }
}
