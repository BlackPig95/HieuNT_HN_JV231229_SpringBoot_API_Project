package com.ra.hieunt_hn_jv231229_project_module4_api.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "wish_list")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WishList
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wish_list_id")
    private Long wishListId;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Product product;
}
