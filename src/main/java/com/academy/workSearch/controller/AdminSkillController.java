package com.academy.workSearch.controller;

import com.academy.workSearch.dto.SkillDTO;
import com.academy.workSearch.dto.SkillDetailsDTO;
import com.academy.workSearch.exceptionHandling.exceptions.NoSuchEntityException;
import com.academy.workSearch.service.SkillService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.*;
import java.util.*;
import java.util.function.BiFunction;

@Validated
@RestController
@RequestMapping("/api/v1/skills/admin")
public class AdminSkillController {
    private final Logger logger = LoggerFactory.getLogger(AdminSkillController.class);
    private final static String MESSAGE_ADD_SKILL_WITH_NAME = "Add skill with name ";
    private final static String MESSAGE_FAILED = " failed!";

    private SkillService skillService;

    @Autowired
    public AdminSkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping("")
    @ApiOperation(value = "Show all skills", notes = "Show information about all skills in DB")
    public ResponseEntity<List<SkillDetailsDTO>> getAll() {
        logger.info("Show all skills");
        List<SkillDetailsDTO> skillsDto = skillService.findAll();
        return ResponseEntity.ok(skillsDto);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Find skill by ID", notes = "Find skill in DB, if skill exist")
    public ResponseEntity<SkillDTO> findById(@ApiParam(value = "ID value for skill you need to retrive", required = true)
                          @PathVariable UUID id) {
        logger.info("Find skill with ID = " + id);
        SkillDTO skillDto = skillService.get(id);
        if (Objects.isNull(skillDto)) {
            logger.error("There is no skill with ID = " + id + " in Database");
            throw new NoSuchEntityException("There is no skill with ID = " + id + " in Database");
        }
        return ResponseEntity.ok(skillDto);
    }

    @PostMapping("")
    @ApiOperation(value = "Add new skill", notes = "Add new skill in DB")
    public ResponseEntity<SkillDTO> add(@RequestBody @Valid SkillDTO skillDto) {
        BiFunction<String, Boolean, String> addSkillStatus = ( name, successful) ->
                MESSAGE_ADD_SKILL_WITH_NAME + name + (successful ? "" : MESSAGE_FAILED);
        this.logger.info(addSkillStatus.apply(skillDto.getName(), true));
        this.skillService.save(skillDto);
        return ResponseEntity.ok(skillDto);
    }

    @PutMapping("")
    @ApiOperation(value = "Update existing skill", notes = "Update existing skill")
    public ResponseEntity<SkillDetailsDTO> update(@RequestBody @Valid SkillDetailsDTO skill) {
        logger.info("Update skill with ID = " + skill.getSkillId());
        return ResponseEntity.ok(this.skillService.update(skill));
    }
}
