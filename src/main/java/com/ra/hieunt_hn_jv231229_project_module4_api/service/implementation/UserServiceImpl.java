package com.ra.hieunt_hn_jv231229_project_module4_api.service.implementation;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.constants.RoleName;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.UserPageableResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.User;
import com.ra.hieunt_hn_jv231229_project_module4_api.objectmapper.UserMapper;
import com.ra.hieunt_hn_jv231229_project_module4_api.repository.IUserRepo;
import com.ra.hieunt_hn_jv231229_project_module4_api.service.design.IUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService
{
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final IUserRepo userRepo;

    @Override
    public Page<UserPageableResponse> findAllUserPageable(Pageable pageable)
    {
        Page<User> userPage = userRepo.findAll(pageable);
        //Index start from 0 => Need to add 1 to balance with total page size
        if (pageable.getPageNumber() + 1 > userPage.getTotalPages())
        {
            throw new RuntimeException("Out of size of user list");
        }
        return userPage.map(UserMapper::toUserPageableResponse);
    }

    @Override
    public List<UserPageableResponse> findAllUsersByName(String fullname)
    {
        List<User> userList = userRepo.findUsersByFullnameContaining(fullname);
        return userList.stream().map(UserMapper::toUserPageableResponse).toList();
    }

    @Override
    public User findUserById(Long id)
    {
        return userRepo.findById(id).orElse(null);
    }

    @Override
    public User lockUserById(Long id)
    {
        User userRoleCheck = findUserById(id);
        if (userRoleCheck == null)
        {
            log.error("User not found");
            throw new NoSuchElementException("Lock not successful. No user found with Id number: " + id);
        }
        if (userRoleCheck.getRoles().stream().anyMatch(role -> role.getRoleName() == RoleName.ROLE_ADMIN))
        {
            throw new RuntimeException("Can't block admin");
        }
        userRoleCheck.setStatus(!userRoleCheck.getStatus());
        userRepo.save(userRoleCheck);
        return userRoleCheck;
    }
}
