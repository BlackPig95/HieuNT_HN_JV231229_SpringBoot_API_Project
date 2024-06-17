package com.ra.hieunt_hn_jv231229_project_module4_api.repository;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.constants.OrderStatus;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
public interface IOrderRepo extends JpaRepository<Order, Long>
{
    @Query("select o, od from Order o join OrderDetail od on o.orderId = od.compositeKey.order.orderId " +
            "where o.orderId = :orderId")
    List<Object[]> getOrderAndDetail(Long orderId);

    List<Order> findOrdersByStatus(OrderStatus status);
}
