package com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request;

import com.ra.hieunt_hn_jv231229_project_module4_api.validator.annotation.PhoneExist;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChangeUserDetailRequest
{
    @NotBlank(message = "Email must not be empty")
    @Pattern(regexp = "^[a-zA-Z0-9]{1,150}@[a-zA-Z0-9]{1,50}[.][a-zA-Z]{1,20}$", message = "Email format incorrect")
    private String email;
    @NotBlank(message = "Full name must not be empty")
    @Length(min = 1, max = 100, message = "Name should not be longer than 100 characters")
    private String fullname;
    private String avatar;
    @NotBlank(message = "Phone must not be empty")
    @Length(min = 10, max = 13, message = "Vietnamese phone number is 10 or 11 digits")
    @Pattern(regexp = "^([+]84|0)[35789][0-9]{8,9}$", message = "Not a Vietnamese number")
    private String phone;
    @NotBlank(message = "Address must not be empty")
    private String address;
    private String enterPassword;
}
