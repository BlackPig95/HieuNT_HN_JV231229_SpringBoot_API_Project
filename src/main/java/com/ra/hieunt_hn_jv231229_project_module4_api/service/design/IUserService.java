package com.ra.hieunt_hn_jv231229_project_module4_api.service.design;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.UserPageableResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserService
{
    Page<UserPageableResponse> findAllUserPageable(Pageable pageable);

    List<UserPageableResponse> findAllUsersByName(String fullname);

    User findUserById(Long id);

    User lockUserById(Long id);
}
