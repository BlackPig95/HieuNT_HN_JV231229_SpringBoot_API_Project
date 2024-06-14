package com.ra.hieunt_hn_jv231229_project_module4_api.controller;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.CustomResponseEntity;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Product;
import com.ra.hieunt_hn_jv231229_project_module4_api.service.design.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api.myservice.com/v1/products")
@RequiredArgsConstructor
public class ProductController
{
    private final IProductService productService;
    private final HttpStatus http = HttpStatus.OK;

    @GetMapping
    public ResponseEntity<?> listProducstSold(@PageableDefault(page = 0, size = 3,
            sort = "productId", direction = Sort.Direction.ASC) Pageable pageable)
    {
        Page<Product> productPage = productService.findProductsPageable(pageable);
        return new ResponseEntity<>(CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(productPage)
                .message("Page number " + (pageable.getPageNumber() + 1) + " / " + productPage.getTotalPages())
                .build(), http);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> findProductById(@PathVariable Long productId)
    {
        return new ResponseEntity<>(CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(productService.findProductById(productId))
                .message("Product Id: " + productId)
                .build(), http);
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<?> findProductsByCat(@PathVariable Long categoryId)
    {
        List<Product> productList = productService.findProductsByCategory(categoryId);
        return new ResponseEntity<>(CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(productList)
                .message("List of products by Category: " + productList.get(0).getCategory().getCategoryName())
                .build(), http);
    }

    @GetMapping("/new-products")
    public ResponseEntity<?> findNewestProducts(@RequestParam(name = "limit", defaultValue = "3") Integer limit)
    {
        return new ResponseEntity<>(CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(productService.findNewestProducts(limit))
                .message(String.format("The latest %s products", limit))
                .build(), http);
    }
}
