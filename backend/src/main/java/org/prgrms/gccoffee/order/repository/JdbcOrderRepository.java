package org.prgrms.gccoffee.order.repository;

import org.prgrms.gccoffee.order.model.Email;
import org.prgrms.gccoffee.order.model.Order;
import org.prgrms.gccoffee.order.model.OrderItem;
import org.prgrms.gccoffee.order.model.OrderStatus;
import org.prgrms.gccoffee.product.model.Category;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static org.prgrms.gccoffee.util.JdbcUtil.toLocalDateTime;
import static org.prgrms.gccoffee.util.JdbcUtil.toUUID;

@Repository
public class JdbcOrderRepository implements OrderRepository {

    private static final RowMapper<OrderItem> orderItemRowMapper = (((resultSet, i) -> {
        UUID productId = toUUID(resultSet.getBytes("product_id"));
        Category category = Category.valueOf(resultSet.getString("category"));
        long price = resultSet.getLong("price");
        int quantity = resultSet.getInt("quantity");
        return new OrderItem(productId, category, price, quantity);
    }));
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<Order> orderRowMapper = ((resultSet, i) -> {
        UUID orderId = toUUID(resultSet.getBytes("order_id"));
        Email email = new Email(resultSet.getString("email"));
        String address = resultSet.getString("address");
        String postcode = resultSet.getString("postcode");
        List<OrderItem> orderItems = findOrderItem(orderId);
        OrderStatus orderStatus = OrderStatus.valueOf(resultSet.getString("order_status"));
        LocalDateTime createdAt = toLocalDateTime(resultSet.getTimestamp("created_at"));
        LocalDateTime updatedAt = toLocalDateTime(resultSet.getTimestamp("updated_at"));
        return new Order(orderId, email, address, postcode, orderItems, orderStatus, createdAt, updatedAt);
    });

    public JdbcOrderRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Order> findAll() {
        return jdbcTemplate.query("SELECT * FROM orders", orderRowMapper);
    }

    @Override
    public List<Order> findByEmail(Email email) {
        return jdbcTemplate.query("SELECT * FROM orders WHERE email = :email"
                , Collections.singletonMap("email", email.getAddress())
                , orderRowMapper);
    }

    @Override
    public List<OrderItem> findOrderItem(UUID orderId) {
        return jdbcTemplate.query("SELECT * FROM order_items WHERE order_id = UUID_TO_BIN(:orderId)"
                , Collections.singletonMap("orderId", orderId.toString().getBytes())
                , orderItemRowMapper);
    }

    @Override
    public Optional<Order> findById(UUID orderId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM orders WHERE order_id = UUID_TO_BIN(:orderId)"
                    , Collections.singletonMap("orderId", orderId.toString().getBytes())
                    , orderRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public Order insert(Order order) {
        jdbcTemplate.update("INSERT INTO orders(order_id, email, address, postcode, order_status, created_at, updated_at) " +
                        "VALUES (UUID_TO_BIN(:orderId), :email, :address, :postcode, :orderStatus, :createdAt, :updatedAt)",
                toOrderParamMap(order));
        order.getOrderItems()
                .forEach(item ->
                        jdbcTemplate.update("INSERT INTO order_items(order_id, product_id, category, price, quantity, created_at, updated_at) " +
                                        "VALUES (UUID_TO_BIN(:orderId), UUID_TO_BIN(:productId), :category, :price, :quantity, :createdAt, :updatedAt)",
                                toOrderItemParamMap(order.getOrderId(), order.getCreatedAt(), order.getUpdatedAt(), item)));
        return order;
    }

    private Map<String, Object> toOrderParamMap(Order order) {
        var paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", order.getOrderId().toString().getBytes());
        paramMap.put("email", order.getEmail().getAddress());
        paramMap.put("address", order.getAddress());
        paramMap.put("postcode", order.getPostcode());
        paramMap.put("orderStatus", order.getOrderStatus().toString());
        paramMap.put("createdAt", order.getCreatedAt());
        paramMap.put("updatedAt", order.getUpdatedAt());
        return paramMap;
    }

    private Map<String, Object> toOrderItemParamMap(UUID orderId, LocalDateTime createdAt, LocalDateTime updatedAt, OrderItem item) {
        var paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", orderId.toString().getBytes());
        paramMap.put("productId", item.getProductId().toString().getBytes());
        paramMap.put("category", item.getCategory().toString());
        paramMap.put("price", item.getPrice());
        paramMap.put("quantity", item.getQuantity());
        paramMap.put("createdAt", createdAt);
        paramMap.put("updatedAt", updatedAt);
        return paramMap;
    }
}
