package com.ra.hieunt_hn_jv231229_project_module4_api.controller;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.CustomResponseEntity;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.ProductResponse;
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
    private final HttpStatus httpOk = HttpStatus.OK;

    @GetMapping//Get list of sold products with pagination and sorting
    public ResponseEntity<?> listProducstSold(@PageableDefault(page = 0, size = 3,
            sort = "productId", direction = Sort.Direction.ASC) Pageable pageable)
    {
        Page<Product> productPage = productService.findProductsPageable(pageable);
        return new ResponseEntity<>(CustomResponseEntity.builder()
                .statusCode(httpOk.value())
                .status(httpOk)
                .data(productPage)
                .message("Page number " + (pageable.getPageNumber() + 1) + " / " + productPage.getTotalPages())
                .build(), httpOk);
    }

    @GetMapping("/{productId}")//Find product based on product Id
    public ResponseEntity<?> findProductById(@PathVariable Long productId)
    {
        return new ResponseEntity<>(CustomResponseEntity.builder()
                .statusCode(httpOk.value())
                .status(httpOk)
                .data(productService.findProductById(productId))
                .message("Product Id: " + productId)
                .build(), httpOk);
    }

    @GetMapping("/categories/{categoryId}")//Find product based on category Id
    public ResponseEntity<?> findProductsByCat(@PathVariable Long categoryId)
    {
        List<Product> productList = productService.findProductsByCategory(categoryId);
        return new ResponseEntity<>(CustomResponseEntity.builder()
                .statusCode(httpOk.value())
                .status(httpOk)
                .data(productList)
                .message("List of products by Category: " + productList.get(0).getCategory().getCategoryName())
                .build(), httpOk);
    }

    @GetMapping("/new-products")//Find out the newest products (latest createdAt)
    public ResponseEntity<?> findNewestProducts(@RequestParam(name = "limit", defaultValue = "3") Integer limit)
    {
        return new ResponseEntity<>(CustomResponseEntity.builder()
                .statusCode(httpOk.value())
                .status(httpOk)
                .data(productService.findNewestProducts(limit))
                .message(String.format("The latest %s products", limit))
                .build(), httpOk);
    }

    @GetMapping("/search")//Search product by name or description
    public ResponseEntity<?> findProductByNameOrDes(@RequestParam(name = "productName", defaultValue = "") String productName,
                                                    @RequestParam(name = "productDes", defaultValue = "") String productDes)
    {
        return new ResponseEntity<>(CustomResponseEntity.builder()
                .statusCode(httpOk.value())
                .status(httpOk)
                .data(productService.findByNameOrDesc(productName, productDes))
                .message("List of products found")
                .build(), httpOk);
    }

    //Find out best-seller / most notable/featured products
    @GetMapping({"/best-seller-products", "/featured-products"})
    public CustomResponseEntity<List<ProductResponse>> bestSellerProducts(@RequestParam(name = "limit", defaultValue = "3") Integer limit)
    {
        return CustomResponseEntity.<List<ProductResponse>>builder()
                .statusCode(httpOk.value())
                .status(httpOk)
                .data(productService.findBestSellerProducts(limit))
                .message("List of best sellers products")
                .build();
    }
}
