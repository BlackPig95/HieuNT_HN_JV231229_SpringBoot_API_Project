package com.ra.hieunt_hn_jv231229_project_module4_api.service.design;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IProductService
{
    Page<Product> findProductsPageable(Pageable pageable);

    Product findProductById(Long id);

    List<Product> findProductsByCategory(Long catId);

    List<Product> findNewestProducts(Integer limit);

    List<Product> findByNameOrDesc(String productName, String productDescription);
}
