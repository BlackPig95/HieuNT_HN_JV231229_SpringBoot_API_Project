package com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.validator.constraints.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductResponse
{
    private Long productId;
    private String productName;
    private String description;
    private Double unitPrice;
    private Integer stockQuantity;
    private Integer soldQuantity;
}
