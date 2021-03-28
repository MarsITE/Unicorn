package com.academy.workSearch.validators;

import com.academy.workSearch.dao.SkillDAO;
import com.academy.workSearch.model.Skill;
import com.academy.workSearch.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueSkillNameValidator implements ConstraintValidator<UniqueSkillName, String> {
    @Autowired
    SkillService skillService;

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        Skill skill = new Skill();
        skill.setName(name);
        return true; //!skillService..get(skill);
    }
}
