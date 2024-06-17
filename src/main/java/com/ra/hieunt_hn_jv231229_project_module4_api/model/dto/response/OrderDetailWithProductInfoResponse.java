package com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
//Support formatting info on the each order detail and the product that came with it
public class OrderDetailWithProductInfoResponse
{
    private Long belongToOrderId;
    private Long withProductId;
    private Double unitPrice;
    private Integer orderQuantity;
    private String productName;
    private String productCategory;
    private Double productPrice;
    private Double totalValuePerOrderDetail;
}
