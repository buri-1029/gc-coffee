package org.prgrms.gccoffee.product.model.dto;

import lombok.Getter;
import org.prgrms.gccoffee.product.model.Category;

@Getter
public class UpdateProductRequest {
    private final String productId;
    private final String productName;
    private final Category category;
    private final long price;
    private final String description;

    public UpdateProductRequest(String productId, String productName, Category category, long price, String description) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.description = description;
    }
}
