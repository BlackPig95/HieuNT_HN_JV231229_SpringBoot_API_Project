package com.ra.hieunt_hn_jv231229_project_module4_api.model.entity;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.constants.RoleName;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "role")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Role
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;
    @Column(name = "role_name")
    @Enumerated(EnumType.STRING)
    private RoleName roleName;
}
