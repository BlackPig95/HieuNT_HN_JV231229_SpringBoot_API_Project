package com.ra.hieunt_hn_jv231229_project_module4_api.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.validator.constraints.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.util.Date;

@Entity
@Table(name = "product")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Product
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;
    @Column(name = "sku", length = 100, unique = true)
    @UUID
    private String sku;
    @Column(name = "product_name", length = 100, nullable = false, unique = true)
    private String productName;
    @Column(name = "description")
    private String description;
    @Column(name = "unit_price", columnDefinition = "Decimal(10,2)")
    private Double unitPrice;
    @Column(name = "stock_quantity")
    @Min(0)
    private Integer stockQuantity;
    @Column(name = "image")
    private String image;
    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date createdAt = new Date();
    @Column(name = "updated_at")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;
}
