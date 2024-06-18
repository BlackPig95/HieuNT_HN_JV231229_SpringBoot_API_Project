package com.ra.hieunt_hn_jv231229_project_module4_api.service.design;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.OrderDetailWithProductInfoResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.OrderWithDetailResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Order;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.OrderDetail;

import java.util.List;
import java.util.Map;

public interface IOrderService
{
    List<Order> findAllOrders();

    Order updateOrderStatus(Long orderId, String newOrderStatus);

    OrderWithDetailResponse findOrderAndDetails(Long orderId);

    List<Order> findOrdersByStatus(String status);

    List<Long> findOrdersIdByUserId(Long userId);
}
