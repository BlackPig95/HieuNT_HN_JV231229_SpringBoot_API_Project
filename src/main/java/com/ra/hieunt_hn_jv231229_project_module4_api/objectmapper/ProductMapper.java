package com.ra.hieunt_hn_jv231229_project_module4_api.objectmapper;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request.ProductRequest;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Category;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Product;
import com.ra.hieunt_hn_jv231229_project_module4_api.repository.ICategoryRepo;
import com.ra.hieunt_hn_jv231229_project_module4_api.repository.IProductRepo;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class ProductMapper
{
    private final ICategoryRepo categoryRepo;

    public Product requestToProduct(ProductRequest productRequest)
    {
        return Product.builder()
                .productId(productRequest.getProductId())
                .sku(productRequest.getSku())
                .productName(productRequest.getProductName())
                .description(productRequest.getDescription())
                .unitPrice(productRequest.getUnitPrice())
                .stockQuantity(productRequest.getStockQuantity())
                .image(productRequest.getImage())
                .createdAt(productRequest.getCreatedAt())
                .updatedAt(productRequest.getUpdatedAt())
                .category(categoryRepo.findById(productRequest.getCategoryId()).orElseThrow(() -> new RuntimeException("Category does not exist")))
                .build();
    }

    public Product updateProductInfo(Long id, ProductRequest productRequest)
    {
        Product updatedProduct = new Product();
        updatedProduct.setProductId(id);
        updatedProduct.setProductName(productRequest.getProductName());
        updatedProduct.setDescription(productRequest.getDescription());
        updatedProduct.setUnitPrice(productRequest.getUnitPrice());
        updatedProduct.setStockQuantity(productRequest.getStockQuantity());
        updatedProduct.setImage(productRequest.getImage());
        //Set updatedAt but no need to set createdAt because this is update action
        updatedProduct.setUpdatedAt(new Date());
        updatedProduct.setCategory(categoryRepo.findById(productRequest.getCategoryId()).orElseThrow(() -> new RuntimeException("Category does not exist")));
        return updatedProduct;
    }
}
