package com.academy.workSearch.controller;

import com.academy.workSearch.exceptionHandling.NoSuchUserException;
import com.academy.workSearch.model.Skill;
import com.academy.workSearch.model.User;
import com.academy.workSearch.service.SkillService;
import com.academy.workSearch.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api1")
@AllArgsConstructor

public class SkillController {
    private final Logger logger = LoggerFactory.getLogger(SkillController.class);

    @Autowired
    private final SkillService skillService;

    @GetMapping("/skills")
    @ApiOperation(value = "Find all skills", notes = "Find skills where value = false")
    public List<Skill> showSkills() {
        logger.info("Show all skills");
        return skillService.findAll();
    }


    @PostMapping("/add_skill")
    @ApiOperation(value = "Add new skill for verification", notes = "Add new skill in DB")
    public Skill addNewSkill(@RequestBody Skill skill) {
        skillService.save(skill);
        logger.info("Add skill with name = " + skill.getSkillId());
        return skill;
    }



}

