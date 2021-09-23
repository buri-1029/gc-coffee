package org.prgrms.gccoffee.product.repository;

import org.prgrms.gccoffee.product.model.Category;
import org.prgrms.gccoffee.product.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    List<Product> findAll();

    List<Product> findByCategory(Category category);

    List<Product> findLikeName(String productName);

    Optional<Product> findById(UUID productId);

    Product insert(Product product);

    Product update(Product product);

    void deleteById(UUID productId);

    void deleteAll();

}
