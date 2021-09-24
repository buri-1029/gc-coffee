package org.prgrms.gccoffee.product.controller;

import org.prgrms.gccoffee.product.model.Product;
import org.prgrms.gccoffee.product.model.dto.CreateProductRequest;
import org.prgrms.gccoffee.product.model.dto.UpdateProductRequest;
import org.prgrms.gccoffee.product.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.UUID;

@Controller
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String productsPage(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "product-list";
    }

    @GetMapping("/new-product")
    public String newProductsPage() {
        return "new-product";
    }


    @PostMapping("/products")
    public String newProduct(CreateProductRequest createProductRequest) {
        productService.createProduct(
                createProductRequest.getProductName(),
                createProductRequest.getCategory(),
                createProductRequest.getPrice(),
                createProductRequest.getDescription());
        return "redirect:/products";
    }

    @GetMapping("/update-product/{productId}")
    public String updateProductPage(@PathVariable("productId") String productId, Model model) {
        Product product = productService.getProductByProductId(UUID.fromString(productId));
        model.addAttribute("product", product);
        return "update-product";
    }

    @PostMapping("/update-product")
    public String updateProduct(UpdateProductRequest updateProductRequest) {
        System.out.println(updateProductRequest.getProductId());
        productService.updateProduct(UUID.fromString(updateProductRequest.getProductId()),
                updateProductRequest.getProductName(),
                updateProductRequest.getCategory(),
                updateProductRequest.getPrice(),
                updateProductRequest.getDescription());
        return "redirect:/products";
    }

    @GetMapping("/delete-product/{productId}")
    public String deleteProduct(@PathVariable("productId") String productId) {
        productService.deleteProduct(UUID.fromString(productId));
        return "redirect:/products";
    }
}
