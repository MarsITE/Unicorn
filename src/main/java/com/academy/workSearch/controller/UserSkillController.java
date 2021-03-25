package com.academy.workSearch.controller;

import com.academy.workSearch.model.Skill;
import com.academy.workSearch.service.SkillService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/1")
@AllArgsConstructor

public class UserSkillController {
    private final Logger logger = LoggerFactory.getLogger(UserSkillController.class);

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

