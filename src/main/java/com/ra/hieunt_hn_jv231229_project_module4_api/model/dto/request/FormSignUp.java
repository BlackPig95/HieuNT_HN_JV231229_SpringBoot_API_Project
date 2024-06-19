package com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request;

import com.ra.hieunt_hn_jv231229_project_module4_api.validator.annotation.PhoneExist;
import com.ra.hieunt_hn_jv231229_project_module4_api.validator.annotation.UserNameExist;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FormSignUp
{
    @NotBlank(message = "Username must not be empty")
    @Length(min = 1, max = 100, message = "Username should not be longer than 100 characters")
    @UserNameExist
    private String username;
    @NotBlank(message = "Email must not be empty")
    @Pattern(regexp = "^[a-zA-Z0-9]{1,150}@[a-zA-Z0-9]{1,50}[.][a-zA-Z]{1,20}$", message = "Email format incorrect")
    private String email;
    @NotBlank(message = "Full name must not be empty")
    @Length(min = 1, max = 100, message = "Name should not be longer than 100 characters")
    private String fullname;
    private Boolean status;
    @NotBlank(message = "Password must not be empty")
    private String password;
    private MultipartFile avatar;
    //    private String avatar;
    @NotBlank(message = "Phone must not be empty")
    @Length(min = 10, max = 13, message = "Vietnamese phone number is 10 or 11 digits")
    @Pattern(regexp = "^([+]84|0)[35789][0-9]{8,9}$", message = "Not a Vietnamese number")
    @PhoneExist
    private String phone;//check unique
    @NotBlank(message = "Address must not be empty")
    private String address;
    //    @DateTimeFormat(pattern = "dd/MM/yyyy")
//    private Date createdAt = new Date();
    //    @DateTimeFormat(pattern = "dd/MM/yyyy")
//    private Date updatedAt;
    private Set<String> roles;
}
