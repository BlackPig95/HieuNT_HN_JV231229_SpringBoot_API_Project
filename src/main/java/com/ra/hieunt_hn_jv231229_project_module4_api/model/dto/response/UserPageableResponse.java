package com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserPageableResponse
{
    private Long userId;
    private String username;
    private String email;
    private String fullname;
    private Boolean status;
    private String avatar;
    private String phone;
    private String address;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date createdAt;
    private Date updatedAt;
}
