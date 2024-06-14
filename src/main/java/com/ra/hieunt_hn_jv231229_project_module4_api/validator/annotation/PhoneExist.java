package com.ra.hieunt_hn_jv231229_project_module4_api.validator.annotation;

import com.ra.hieunt_hn_jv231229_project_module4_api.validator.handler.HandlePhoneExist;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = HandlePhoneExist.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneExist
{
    String message() default "Phone number is used, please choose another number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
    @Retention(RUNTIME)
    public @interface List
    {
        PhoneExist[] value();
    }
}
