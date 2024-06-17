package com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.constants.OrderStatus;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartCheckoutRequest
{
    @Length(max = 100, message = "Note should not be longer than 100 characters")
    private String note;
    @Length(max = 100, message = "Your desired receiver's name should not be longer than 100 characters")
    private String receiveName;
    @Length(max = 100, message = "Your desired receiver's address should not be longer than 255 characters")
    private String receiveAddress;
    @Length(max = 15, message = "Phone number too long")
    @Pattern(regexp = "^([+]84|0)[35789][0-9]{8,9}$", message = "Not a Vietnamese number")
    private String receivePhone;
}
