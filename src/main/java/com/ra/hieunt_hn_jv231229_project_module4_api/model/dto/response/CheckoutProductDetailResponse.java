package com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CheckoutProductDetailResponse
{
    private String productName;
    private String description;
    private Double pricePerProduct;
    private String image;
    private String productCategoryName;
}
