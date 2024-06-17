package com.ra.hieunt_hn_jv231229_project_module4_api.service.implementation;

import com.ra.hieunt_hn_jv231229_project_module4_api.exception.CustomException;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.constants.RoleName;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request.FormSignIn;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request.FormSignUp;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.JwtUserResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Role;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.User;
import com.ra.hieunt_hn_jv231229_project_module4_api.repository.IUserRepo;
import com.ra.hieunt_hn_jv231229_project_module4_api.security.jwt.JwtProvider;
import com.ra.hieunt_hn_jv231229_project_module4_api.security.principal.UserDetailCustom;
import com.ra.hieunt_hn_jv231229_project_module4_api.service.design.IAuthService;
import com.ra.hieunt_hn_jv231229_project_module4_api.service.design.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService
{
    private final IRoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final IUserRepo userRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Override
    public void signUp(FormSignUp formSignUp)
    {
        Set<Role> roleSet = new HashSet<>();
        if (formSignUp.getRoles() == null || formSignUp.getRoles().isEmpty())
        { //If no role is provided => Add user role
            roleSet.add(roleService.findByRoleName(RoleName.ROLE_USER));
        } else
        {
            formSignUp.getRoles().forEach(role -> roleSet.add(roleService.findByRoleName(RoleName.valueOf(role))));
        }
        User newUser = User.builder()
                .username(formSignUp.getUsername())
                .email(formSignUp.getEmail())
                .fullname(formSignUp.getFullname())
                .status(formSignUp.getStatus())
                .password(passwordEncoder.encode(formSignUp.getPassword()))
                .avatar(formSignUp.getAvatar() != null ? formSignUp.getAvatar() : "")
                .phone(formSignUp.getPhone())
                .address(formSignUp.getAddress())
                .createdAt(formSignUp.getCreatedAt())
                .roles(roleSet)
                .build();
        userRepo.save(newUser);
    }

    @Override
    public JwtUserResponse signIn(FormSignIn formSignIn) throws CustomException
    {
        Authentication authentication;
        try
        {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(formSignIn.getUsername(), formSignIn.getPassword()));
        } catch (AuthenticationException e)
        {
            throw new CustomException("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
        UserDetailCustom userDetailCustom = (UserDetailCustom) authentication.getPrincipal();
        String accessToken = jwtProvider.createToken(userDetailCustom);
        return JwtUserResponse.builder()
                .token(accessToken)
                .username(userDetailCustom.getUsername())
                .email(userDetailCustom.getEmail())
                .fullname(userDetailCustom.getFullname())
//                .password(userDetailCustom.getPassword())
                .status(userDetailCustom.getStatus())
                .avatar(userDetailCustom.getAvatar())
                .phone(userDetailCustom.getPhone())
                .address(userDetailCustom.getAddress())
                .createdAt(userDetailCustom.getCreatedAt())
                .updatedAt(userDetailCustom.getUpdatedAt())
                .roles(userDetailCustom.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()))
                .build();
    }
}
