package org.prgrms.gccoffee.product.repository;

import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.ScriptResolver;
import com.wix.mysql.config.Charset;
import com.wix.mysql.config.MysqldConfig;
import org.junit.jupiter.api.*;
import org.prgrms.gccoffee.product.model.Category;
import org.prgrms.gccoffee.product.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v8_0_11;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JdbcProductRepositoryTest {

    private static final Product newProduct = new Product(UUID.randomUUID(), "new-product", Category.COFFEE_BEAN_PACKAGE, 1000L);

    static EmbeddedMysql embeddedMysql;

    @Autowired
    ProductRepository productRepository;

    @BeforeAll
    static void setup() {
        MysqldConfig config = aMysqldConfig(v8_0_11)
                .withCharset(Charset.UTF8)
                .withPort(2215)
                .withUser("test", "test1234!")
                .withTimeZone("Asia/Seoul")
                .build();
        embeddedMysql = anEmbeddedMysql(config)
                .addSchema("test-order-mgmt", ScriptResolver.classPathScripts("schema.sql"))
                .start();
    }

    @AfterAll
    static void cleanup() {
        embeddedMysql.stop();
    }

    @Test
    @Order(1)
    @DisplayName("커피 상품을 추가할 수 있다.")
    void insertTest() {
        productRepository.insert(newProduct);

        Optional<Product> retrievedProduct = productRepository.findById(newProduct.getProductId());

        assertThat(retrievedProduct).isNotEmpty();
    }

    @Test
    @Order(2)
    @DisplayName("중복된 상품 이름으로 상품을 추가할 수 없다.")
    void insertWithDuplicateNameTest() {
        assertThatThrownBy(() -> productRepository.insert(newProduct))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("This product name already exists");
    }

    @Test
    @Order(3)
    @DisplayName("상품 아이디로 커피 상품 정보를 조회할 수 있다.")
    void findByIdTest() {
        Optional<Product> retrievedProduct = productRepository.findById(newProduct.getProductId());

        assertThat(retrievedProduct).isNotEmpty();
    }

    @Test
    @Order(4)
    @DisplayName("상품 이름을 포함한 커피 상품 정보를 조회할 수 있다.")
    void findByNameTest() {

    }

    @Test
    @Order(5)
    @DisplayName("상품들을 카테고리로 조회할 수 있다.")
    void findByCategoryTest() {
        List<Product> retrievedProduct = productRepository.findByCategory(Category.COFFEE_BEAN_PACKAGE);

        assertThat(retrievedProduct).hasSize(1);
    }

    @Test
    @Order(6)
    @DisplayName("커피 상품의 정보를 수정할 수 있다.")
    void updateTest() {
        Product updateProduct = new Product(newProduct.getProductId(), "update-product", Category.COFFEE_BEAN_PACKAGE, 2000L, "to update",
                newProduct.getCreatedAt(), LocalDateTime.now());

        productRepository.update(updateProduct);

        Optional<Product> retrievedProduct = productRepository.findById(newProduct.getProductId());

        assertThat(retrievedProduct).isNotEmpty();
        assertThat(retrievedProduct.get().getProductName()).isEqualTo("update-product");
        assertThat(retrievedProduct.get().getPrice()).isEqualTo(2000L);
    }

    @Test
    @Order(7)
    @DisplayName("모든 상품을 삭제할 수 있다.")
    void deleteAllTest() {
        productRepository.deleteAll();

        List<Product> retrievedProduct = productRepository.findAll();

        assertThat(retrievedProduct).isEmpty();
    }
}

