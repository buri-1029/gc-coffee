package org.prgrms.gccoffee.product.service;

import org.prgrms.gccoffee.product.model.Category;
import org.prgrms.gccoffee.product.model.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<Product> getAllProducts();

    List<Product> getProductsByCategory(Category category);

    List<Product> getProductsByProductName(String productName);

    Product getProductByProductId(UUID productId);

    Product createProduct(String productName, Category category, long price);

    Product createProduct(String productName, Category category, long price, String description);

    Product updateProduct(UUID productId, String productName, Category category, long price, String description);

    void deleteProduct(UUID productId);
}
