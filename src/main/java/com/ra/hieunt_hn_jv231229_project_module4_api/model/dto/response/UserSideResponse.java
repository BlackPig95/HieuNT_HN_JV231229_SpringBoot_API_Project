package com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response;

import jakarta.persistence.Column;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserSideResponse
{
    private String username;
    private String email;
    private String fullname;
    private String password;
    private String avatar;
    private String phone;
    private String address;
    private String accessToken;
}
