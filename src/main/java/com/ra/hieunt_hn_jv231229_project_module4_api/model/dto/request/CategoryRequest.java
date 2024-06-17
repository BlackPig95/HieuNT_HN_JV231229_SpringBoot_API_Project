package com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CategoryRequest
{
    @NotBlank(message = "Category name must not be blanked")
    @Length(max = 100, message = "Category name should not be longer than 100 characters")
    private String categoryName;
    private String description;
    private Boolean status = true;
}
