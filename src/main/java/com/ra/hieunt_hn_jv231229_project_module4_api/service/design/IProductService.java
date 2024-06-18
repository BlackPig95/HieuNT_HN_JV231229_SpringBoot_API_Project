package com.ra.hieunt_hn_jv231229_project_module4_api.service.design;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request.ProductRequest;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.MostLikedProductResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.ProductResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.ProductSoldResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface IProductService
{   //Find products that were sold. Called by ProductController
    Page<Product> findProductsPageable(Pageable pageable);

    Product findProductById(Long id);

    List<Product> findProductsByCategory(Long catId);

    List<Product> findNewestProducts(Integer limit);

    List<Product> findByNameOrDesc(String productName, String productDescription);

    List<ProductResponse> findBestSellerProducts(Integer limit);

    Product addProduct(ProductRequest productRequest);

    //Find all products. Called by AdminController
    Page<Product> findAllProductsAvailable(Pageable pageable);

    Product deleteProduct(Long id);

    Product updateProduct(Long id, ProductRequest productRequest);

    Product findById(Long productId);

    List<ProductSoldResponse> findBestSellerInTime(Date from, Date to);

    List<MostLikedProductResponse> findMostLikedProducts();
}
