package com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChangePasswordRequest
{
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
