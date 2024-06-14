package com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FormSignIn
{
    @NotBlank(message = "Username must not be empty")
    @Length(min = 1, max = 100, message = "Username should not be longer than 100 characters")
    private String username;
    @NotBlank(message = "Password must not be empty")
    private String password;
}
