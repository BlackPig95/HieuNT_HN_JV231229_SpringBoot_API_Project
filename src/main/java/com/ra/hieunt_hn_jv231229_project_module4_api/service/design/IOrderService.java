package com.ra.hieunt_hn_jv231229_project_module4_api.service.design;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.OrderWithDetailResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.RevenueTimeResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Order;

import java.util.Date;
import java.util.List;

public interface IOrderService
{
    List<Order> findAllOrders();

    Order updateOrderStatus(Long orderId, String newOrderStatus);

    OrderWithDetailResponse findOrderAndDetails(Long orderId);

    List<Order> findOrdersByStatus(String status);

    List<Long> findOrdersIdByUserId(Long userId);

    //Revenue in time period
    RevenueTimeResponse findRevenueInTime(Date from, Date to);

    //Only calculate the number of invoices in time period
    Integer findNumberOfInvoicesInTimePeriod(Date from, Date to);
}
