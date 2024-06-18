package com.ra.hieunt_hn_jv231229_project_module4_api.repository;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderDetailrepo extends JpaRepository<OrderDetail, Long>
{
    @Query("select sum(o.orderQuantity) from OrderDetail o where " +
            "o.compositeKey.product.productId = :productId " +
            "group by o.compositeKey.product.productId")
    Integer findQuantityPerProduct(Long productId);

    List<OrderDetail> findAllByCompositeKeyOrderOrderId(Long orderId);

}
