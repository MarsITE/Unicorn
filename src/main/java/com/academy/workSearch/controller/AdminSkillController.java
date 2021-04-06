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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiFunction;

@Validated
@RestController
@RequestMapping("/api/v1/admin/skills")
public class AdminSkillController {
    private final Logger logger = LoggerFactory.getLogger(AdminSkillController.class);
    private SkillService skillService;

    @Autowired
    public AdminSkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping()
    @ApiOperation(value = "Show all skills", notes = "Show information about all skills")
    public ResponseEntity<List<SkillDetailsDTO>> getAll() {
        logger.info("Show all skills");
        List<SkillDetailsDTO> skillsDto = skillService.findAll();
        return ResponseEntity.ok(skillsDto);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Find skill by ID", notes = "Find skill if exists")
    public ResponseEntity<SkillDetailsDTO> findById(@ApiParam(value = "ID value for skill you need to retrieve", required = true)
                                             @PathVariable UUID id) {
        logger.info("Find skill with ID = {}", id);
        SkillDetailsDTO skillDto = skillService.get(id);
        return ResponseEntity.ok(skillDto);
    }

    @PostMapping()
    @ApiOperation(value = "Add new skill", notes = "Add new skill")
    public ResponseEntity<SkillDTO> add(@RequestBody @Valid SkillDTO skillDto) {
        logger.info("Add skill with name " + skillDto.getName());
        skillService.save(skillDto);
        return ResponseEntity.ok(skillDto);
    }

    @PutMapping()
    @ApiOperation(value = "Update existing skill", notes = "Update existing skill")
    public ResponseEntity<SkillDetailsDTO> update(@RequestBody @Valid SkillDetailsDTO skill) {
        logger.info("Update skill with ID = {}", skill.getSkillId());
        return ResponseEntity.ok(this.skillService.update(skill));
    }
}
