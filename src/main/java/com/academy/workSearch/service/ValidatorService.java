package com.academy.workSearch.service;

import javax.validation.*;
import java.util.HashSet;
import java.util.Set;

public class ValidatorService<E> {

    public Set<ValidationException> validate(E e) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<E>> violations = validator.validate(e);
        Set<ValidationException> exceptions = new HashSet<>();
        for (ConstraintViolation<E> violation : violations) {
            exceptions.add(new ValidationException(violation.getInvalidValue().toString()));
        }
        return exceptions;
    }

}
