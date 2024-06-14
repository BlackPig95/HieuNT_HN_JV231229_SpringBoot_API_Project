package com.ra.hieunt_hn_jv231229_project_module4_api.validator.annotation;

import com.ra.hieunt_hn_jv231229_project_module4_api.validator.handler.HandlePhoneExist;
import com.ra.hieunt_hn_jv231229_project_module4_api.validator.handler.HandleUserNameExist;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = HandleUserNameExist.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserNameExist
{
    String message() default "This username is already chosen, please choose another username";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
    @Retention(RUNTIME)
    public @interface List
    {
        UserNameExist[] value();
    }
}
