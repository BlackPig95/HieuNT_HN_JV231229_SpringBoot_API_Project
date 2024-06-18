package com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Product;
import jakarta.persistence.Column;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CategoryRevenueResponse
{
    private Long categoryId;
    private String categoryName;
    private String description;
    private Double totalRevenue;
}
