package com.ra.hieunt_hn_jv231229_project_module4_api.objectmapper;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.UserPageableResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.User;

public class UserMapper
{
    private UserMapper()
    {
    }

    ;

    public static UserPageableResponse toUserPageableResponse(User user)
    {
        return UserPageableResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullname(user.getFullname())
                .status(user.getStatus())
                .avatar(user.getAvatar())
                .phone(user.getPhone())
                .address(user.getAddress())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
