package com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.validator.constraints.UUID;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ShoppingCartDetailsResponse
{
    private Integer orderQuantity;
    private String productName;
    private Double unitPrice;
    private String description;
    private String image;
}
