package com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AddressRequest//Also used for some response, because this class contained enough info to return to the API call's view
{
    @NotBlank(message = "Address must not be empty")
    @Length(max = 255, message = "Your address should not be longer than 255 characters")
    private String fullAddress;
    @Length(min = 10, max = 13, message = "Vietnamese phone number is 10 or 11 digits")
    @Pattern(regexp = "^([+]84|0)[35789][0-9]{8,9}$", message = "Not a Vietnamese number")
    private String phone;
    @NotBlank(message = "Receiver's name must not be empty")
    @Length(max = 50, message = "Your receiver's name should not be longer than 50 characters")
    private String receiveName;
}
