package com.ra.hieunt_hn_jv231229_project_module4_api.controller;

import com.ra.hieunt_hn_jv231229_project_module4_api.exception.CustomException;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request.FormSignIn;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request.FormSignUp;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.CustomResponseEntity;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.JwtUserResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.service.design.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api.myservice.com/v1/auth")
@RequiredArgsConstructor
public class AuthController
{
    private final IAuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody FormSignUp formSignUp)
    {
        authService.signUp(formSignUp);
        return new ResponseEntity<>(CustomResponseEntity.builder()
                .statusCode(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED)
                .data("Signed up successfully")
                .build(), HttpStatus.CREATED);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody FormSignIn formSignIn) throws CustomException
    {
        JwtUserResponse userSignedin = authService.signIn(formSignIn);
        return new ResponseEntity<>(CustomResponseEntity.<JwtUserResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(userSignedin)
                .message("Login successfully")
                .build(), HttpStatus.OK);
    }
}
