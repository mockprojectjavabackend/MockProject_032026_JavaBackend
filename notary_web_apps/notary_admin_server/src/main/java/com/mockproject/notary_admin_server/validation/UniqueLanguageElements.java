package com.mockproject.notary_admin_server.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueElementsValidator.class)
public @interface UniqueLanguageElements {
    String message() default "Languages must not contain duplicate";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
