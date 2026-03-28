package com.mockproject.notary_admin_server.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class UniqueElementsValidator implements ConstraintValidator<UniqueLanguageElements, Collection<?>> {

    @Override
    public boolean isValid(Collection<?> objects, ConstraintValidatorContext constraintValidatorContext) {
        if(objects == null || objects.size() == 0) return true;

        List<String> normalized = objects.stream()
                .map(obj -> obj == null ? null : obj.toString().trim().toLowerCase())
                .toList();

        if (normalized.contains(null)) return false;
        return normalized.size() == new HashSet<>(normalized).size();
    }
}
