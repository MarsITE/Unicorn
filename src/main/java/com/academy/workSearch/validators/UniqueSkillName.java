package com.academy.workSearch.validators;

import javax.validation.Payload;

public @interface UniqueSkillName {
    String message() default "Such skill is already present";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default{};
}