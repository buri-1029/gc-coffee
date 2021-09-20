package org.prgrms.gccoffee.product.repository;

import org.junit.jupiter.api.TestInstance;
import org.prgrms.gccoffee.product.model.Category;
import org.prgrms.gccoffee.product.model.Product;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

import static org.prgrms.gccoffee.util.JdbcUtil.toLocalDateTime;
import static org.prgrms.gccoffee.util.JdbcUtil.toUUID;

@Repository
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JdbcProductRepository implements ProductRepository {

    private static final RowMapper<Product> productRowMapper = ((resultSet, i) -> {
        UUID productId = toUUID(resultSet.getBytes("product_id"));
        String productName = resultSet.getString("product_name");
        Category category = Category.valueOf(resultSet.getString("category"));
        long price = resultSet.getLong("price");
        String description = resultSet.getString("description");
        LocalDateTime createdAt = toLocalDateTime(resultSet.getTimestamp("created_at"));
        LocalDateTime updatedAt = toLocalDateTime(resultSet.getTimestamp("updated_at"));
        return new Product(productId, productName, category, price, description, createdAt, updatedAt);
    });

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcProductRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query("SELECT * FROM products", productRowMapper);
    }

    @Override
    public List<Product> findByCategory(Category category) {
        return jdbcTemplate.query("SELECT * FROM products WHERE category = :category"
                , Collections.singletonMap("category", category.toString())
                , productRowMapper);
    }

    @Override
    public Optional<Product> findById(UUID productId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM products WHERE product_id = UUID_TO_BIN(:productId)"
                    , Collections.singletonMap("productId", productId.toString().getBytes())
                    , productRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Product> findByName(String productName) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM products WHERE product_name = :productName"
                    , Collections.singletonMap("productName", productName)
                    , productRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Product insert(Product product) {
        int update = jdbcTemplate.update("INSERT INTO products(product_id, product_name, category, price, description, created_at, updated_at)" +
                " VALUES (UUID_TO_BIN(:productId), :productName, :category, :price, :description, :createdAt, :updatedAt)", toParamMap(product));
        if (update != 1) {
            throw new RuntimeException("Noting was inserted");
        }
        return product;
    }

    @Override
    public Product update(Product product) {
        int update = jdbcTemplate.update(
                "UPDATE products SET product_name = :productName, category = :category, price = :price, description = :description, created_at = :createdAt, updated_at = :updatedAt" +
                        " WHERE product_id = UUID_TO_BIN(:productId)", toParamMap(product));
        if (update != 1) {
            throw new RuntimeException("Nothing was updated");
        }
        return product;
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM products", Collections.emptyMap());
    }


    private Map<String, Object> toParamMap(Product product) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("productId", product.getProductId().toString().getBytes());
        paramMap.put("productName", product.getProductName());
        paramMap.put("category", product.getCategory().toString());
        paramMap.put("price", product.getPrice());
        paramMap.put("description", product.getDescription());
        paramMap.put("createdAt", product.getCreatedAt());
        paramMap.put("updatedAt", product.getUpdatedAt());
        return paramMap;
    }

}
