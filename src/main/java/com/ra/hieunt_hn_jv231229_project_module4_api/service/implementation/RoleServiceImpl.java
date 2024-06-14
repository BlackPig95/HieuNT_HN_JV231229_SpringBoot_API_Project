package com.ra.hieunt_hn_jv231229_project_module4_api.service.implementation;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.constants.RoleName;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Role;
import com.ra.hieunt_hn_jv231229_project_module4_api.repository.IRoleRepo;
import com.ra.hieunt_hn_jv231229_project_module4_api.service.design.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService
{
    private final IRoleRepo roleRepo;

    @Override
    public Role findByRoleName(RoleName roleName)
    {
        return roleRepo.findByRoleName(roleName).orElseThrow(() -> new NoSuchElementException("Role not found"));
    }

    @Override
    public List<Role> findAll()
    {
        return roleRepo.findAll();
    }
}
