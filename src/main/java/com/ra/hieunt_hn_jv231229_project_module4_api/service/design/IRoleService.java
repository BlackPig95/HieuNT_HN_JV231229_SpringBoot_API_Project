package com.ra.hieunt_hn_jv231229_project_module4_api.service.design;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.constants.RoleName;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Role;

import java.util.List;

public interface IRoleService
{
    Role findByRoleName(RoleName roleName);

    List<Role> findAll();
}
