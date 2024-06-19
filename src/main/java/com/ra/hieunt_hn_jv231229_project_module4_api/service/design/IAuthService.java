package com.ra.hieunt_hn_jv231229_project_module4_api.service.design;

import com.ra.hieunt_hn_jv231229_project_module4_api.exception.CustomException;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request.FormSignIn;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request.FormSignUp;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.JwtUserResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.User;

public interface IAuthService
{
    User signUp(FormSignUp formSignUp);

    JwtUserResponse signIn(FormSignIn formSignIn) throws CustomException;
}
