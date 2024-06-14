package com.ra.hieunt_hn_jv231229_project_module4_api.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_detail")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetail
{
    @EmbeddedId
    private OrderDetailCompositeKey compositeKey;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "unit_price", columnDefinition = "Decimal(10,2)")
    private Double unitPrice;
    @Column(name = "order_quantity") // check>0
    private Integer orderQuantity;
}
