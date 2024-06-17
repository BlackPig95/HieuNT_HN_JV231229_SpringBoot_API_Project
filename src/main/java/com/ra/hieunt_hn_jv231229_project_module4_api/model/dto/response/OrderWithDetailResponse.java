package com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.constants.OrderStatus;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Order;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.OrderDetail;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
//Formatting the info to return to the API request
public class OrderWithDetailResponse
{
    //    private Order order;
    private Long orderId;
    private String serialNumber;
    private Double totalPrice;
    private String status;
    private String note;
    private String receiveName;
    private String receiveAddress;
    private String receivePhone;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date createdAt;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date receivedAt;
    private String customerName;
    private List<OrderDetailWithProductInfoResponse> orderDetailList;
//    private List<OrderDetail> orderDetailList;
//    private String productName;
//    private String productCategory;
//    private Double productPrice;
//    private Integer productOrderedQuantity;
//    private Double unitPricePerOrderDetail;
}
