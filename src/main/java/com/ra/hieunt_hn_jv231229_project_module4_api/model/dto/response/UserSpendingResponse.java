package com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserSpendingResponse
{
    private Long userId;
    private String username;
    private String email;
    private String fullname;
    private Boolean status;
    private String avatar;
    private String phone;
    private String address;
    private Set<Role> roles;
    private Double totalSpending;
}
