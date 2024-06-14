package com.ra.hieunt_hn_jv231229_project_module4_api.validator.handler;

import com.ra.hieunt_hn_jv231229_project_module4_api.repository.IUserRepo;
import com.ra.hieunt_hn_jv231229_project_module4_api.validator.annotation.UserNameExist;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HandleUserNameExist implements ConstraintValidator<UserNameExist, String>
{
    private final IUserRepo userRepo;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context)
    {
        return !userRepo.existsByUsername(username);
    }
}
