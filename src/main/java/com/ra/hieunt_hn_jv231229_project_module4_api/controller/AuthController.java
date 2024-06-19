package com.ra.hieunt_hn_jv231229_project_module4_api.controller;

import com.ra.hieunt_hn_jv231229_project_module4_api.exception.CustomException;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.constants.RoleName;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request.FormSignIn;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request.FormSignUp;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.CustomResponseEntity;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.JwtUserResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.UserSideResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.service.design.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/auth")
@RequiredArgsConstructor
public class AuthController
{
    private final IAuthService authService;

    @PostMapping(value = "/sign-up")
    public ResponseEntity<?> signUp(@Valid @ModelAttribute FormSignUp formSignUp)
    {
        return new ResponseEntity<>(CustomResponseEntity.builder()
                .statusCode(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED)
                .data(authService.signUp(formSignUp))
                .message("Signed up successfully")
                .build(), HttpStatus.CREATED);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@Valid @RequestBody FormSignIn formSignIn) throws CustomException
    {
        JwtUserResponse userSignedIn = authService.signIn(formSignIn);

        return new ResponseEntity<>(CustomResponseEntity.builder()
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(userSignedIn)
                .message("Login successfully")
                .build(), HttpStatus.OK);
    }
}
