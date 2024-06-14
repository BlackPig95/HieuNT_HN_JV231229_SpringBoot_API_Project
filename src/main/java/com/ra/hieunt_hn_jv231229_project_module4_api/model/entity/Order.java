package com.ra.hieunt_hn_jv231229_project_module4_api.model.entity;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.constants.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Order
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;
    @Column(name = "serial_number", length = 100)
    private String serialNumber;
    @Column(name = "total_price", columnDefinition = "Decimal(10,2)")
    private Double totalPrice;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @Column(name = "note", length = 100)
    private String note;
    @Column(name = "receive_name", length = 100)
    private String receiveName;
    @Column(name = "receive_address", length = 255)
    private String receiveAddress;
    @Column(name = "receive_phone", length = 15)
    private String receivePhone;
    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    private Date createdAt = new Date();
    @Column(name = "received_at")
    @Temporal(TemporalType.DATE)
    private Date receivedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
}
