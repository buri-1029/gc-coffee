package org.prgrms.gccoffee.product.service;

import org.prgrms.gccoffee.product.model.Category;
import org.prgrms.gccoffee.product.model.Product;
import org.prgrms.gccoffee.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public Product getProductByProductId(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("해당 상품 아이디를 찾을 수 없습니다." + productId));
    }

    @Override
    public Product getProductByProductName(String productName) {
        return productRepository.findByName(productName)
                .orElseThrow(() -> new RuntimeException("해당 상품 이름을 찾을 수 없습니다." + productName));
    }

    @Override
    public Product createProduct(String productName, Category category, long price) {
        return productRepository.insert(new Product(UUID.randomUUID(), productName, category, price));
    }

    @Override
    public Product createProduct(String productName, Category category, long price, String description) {
        return productRepository.insert(new Product(UUID.randomUUID(), productName, category, price, description, LocalDateTime.now(), LocalDateTime.now()));
    }

    @Override
    public Product updateProduct(UUID productId, String productName, Category category, long price, String description) {
        Product product = getProductByProductId(productId);
        return productRepository.insert(new Product(product.getProductId(), productName, category, price, description, product.getCreatedAt(), LocalDateTime.now()));
    }

}
