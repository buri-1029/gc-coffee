package org.prgrms.gccoffee.product.controller;

import org.prgrms.gccoffee.product.model.Product;
import org.prgrms.gccoffee.product.model.dto.CreateProductRequest;
import org.prgrms.gccoffee.product.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

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

}
