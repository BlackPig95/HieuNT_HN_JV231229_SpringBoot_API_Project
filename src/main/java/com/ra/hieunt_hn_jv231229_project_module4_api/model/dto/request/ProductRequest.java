package com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Category;
import com.ra.hieunt_hn_jv231229_project_module4_api.validator.annotation.ProductNameExist;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UUID;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductRequest
{
    private Long productId;
    @UUID(message = "Sku should be left empty. Will be automatically assigned to avoid conflict and duplicated")
    private String sku;
    @Column(name = "product_name", length = 100, nullable = false, unique = true)
    @Length(max = 100, message = "Product name should not be longer than 100 characters")
    @ProductNameExist
    private String productName;
    private String description;
    private Double unitPrice;
    @Min(value = 0, message = "Stock quantity can't be smaller than 0")
    private Integer stockQuantity;
    //    private String image;
    private MultipartFile image;
    private Date createdAt = new Date();
    private Date updatedAt;
    private Long categoryId;
}
