package com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CustomResponseEntity<T>
{
    private Integer statusCode;
    private HttpStatus status;
    private String message;
    private T data;
}
