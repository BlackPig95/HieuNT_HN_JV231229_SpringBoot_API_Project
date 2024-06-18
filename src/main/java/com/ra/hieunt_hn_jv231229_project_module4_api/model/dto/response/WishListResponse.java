package com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WishListResponse
{
    private Long productId;
    private String productName;
    private String productDescription;
    private String productImageUrl;
    private Double productPrice;
    private String productCategory;
}
