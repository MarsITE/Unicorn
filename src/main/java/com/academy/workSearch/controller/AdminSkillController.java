package com.academy.workSearch.controller;

import com.academy.workSearch.exceptionHandling.NoSuchSkillException;
import com.academy.workSearch.model.Skill;
import com.academy.workSearch.service.SkillService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


@Validated
@RestController
@RequestMapping("/api/1/skills/admin")
@AllArgsConstructor
public class AdminSkillController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private final SkillService skillService;

    @GetMapping("/")
    @ApiOperation(value = "Show all skills", notes = "Show information about all skills in DB")
    public List<Skill> showSkills() {
        logger.info("Show all skills");
        return skillService.findAll();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Find skill by ID", notes = "Find skill in DB, if skill exist")
    public Skill getSkill(@ApiParam(value = "ID value for skill you need to retrive", required = true)
                          @PathVariable UUID id) {
        Skill skill = skillService.get(id);
        logger.info("Find skill with ID = " + id);
        if (skill == null) {
            logger.error("There is no skill with ID = " + id + " in Database");
            throw new NoSuchSkillException("There is no skill with ID = " + id + " in Database");
        }
        return skill;
    }

    @PostMapping("/")
    @ApiOperation(value = "Add new skill", notes = "Add new skill in DB")
    public Skill addNewSkill(@RequestBody @Valid Skill skill) {
        skillService.save(skill);
        logger.info("Add skill with ID = " + skill.getSkillId());
        return skill;
    }

    @PutMapping("/")
    @ApiOperation(value = "Update existing skill", notes = "Update existing skill")
    public Skill updateSkill(@RequestBody @Valid Skill skill) {
        skillService.save(skill);
        logger.info("Update skill with ID = " + skill.getSkillId());
        return skill;
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete existing skill", notes = "Delete existing skill")
    public void deleteUser(@Valid @PathVariable UUID id) {
        Skill skill = skillService.get(id);
        if (skill == null) {
            logger.error("There is no skill with ID = " + id + " in Database");
            throw new NoSuchSkillException("There is no skill with ID = " + id + " in Database");
        }
        skillService.delete(id);
        logger.info("Delete user with ID = " + skill.getSkillId());
    }
}
