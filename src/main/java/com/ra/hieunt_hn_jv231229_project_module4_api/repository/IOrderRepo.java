package com.ra.hieunt_hn_jv231229_project_module4_api.repository;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.constants.OrderStatus;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface IOrderRepo extends JpaRepository<Order, Long>
{
    @Query("select o, od from Order o join OrderDetail od on o.orderId = od.compositeKey.order.orderId " +
            "where o.orderId = :orderId")
    List<Object[]> getOrderAndDetail(Long orderId);

    List<Order> findOrdersByStatus(OrderStatus status);

    @Query("select o.orderId from Order o where o.user.userId = :userId")
    List<Long> findOrdersIdByUserId(Long userId);

    //Select only successful order for revenue calculation
    @Query("select o from Order o where o.receivedAt between :from and :to and o.status = 'SUCCESS'")
    List<Order> findOrderInTime(Date from, Date to);

    @Query("select o from Order o where o.status='SUCCESS'")
    List<Order> findOrdersByStatusSuccess();

    @Query("select sum(o.totalPrice) from Order o where o.user.userId = :userId group by o.user.userId")
    Double findTotalPricePerUser(Long userId);
}
