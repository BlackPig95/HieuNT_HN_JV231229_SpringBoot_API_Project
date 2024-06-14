package com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class JwtUserResponse
{
    private String username;
    private String email;
    private String fullname;
    private Boolean status;
    private String avatar;
    private String phone;//check unique
    private String address;
    private Date createdAt = new Date();
    private Date updatedAt;
    private Set<String> roles;
    private String token;
}
