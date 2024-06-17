package com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductInCartRequest
{
    private Long productId;
    private Integer quantity;
}
