package com.academy.workSearch.controller;

import com.academy.workSearch.dto.SkillDTO;
import com.academy.workSearch.dto.SkillDetailsDTO;
import com.academy.workSearch.exceptionHandling.exceptions.NotUniqueEntityException;
import com.academy.workSearch.model.User;
import com.academy.workSearch.service.SkillService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

@Validated
@RestController
@RequestMapping("/api/v1")
public class SkillController {
    private final Logger logger = LoggerFactory.getLogger(SkillController.class);
    private SkillService skillService;

    @Autowired
    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping("/skills")
    @ApiOperation(value = "Show all skills", notes = "Show information about all skills")
    public ResponseEntity<List<SkillDTO>> getAll() {
        logger.info("Show all skills");
        return ResponseEntity.ok(skillService.findAllEnabled(true));
    }

    @GetMapping("/worker/skills")
    @ApiOperation(value = "Show all skills", notes = "Show information about all skills")
    public ResponseEntity<List<SkillDTO>> getAllByUserID(@AuthenticationPrincipal User user) {
        UUID userId = user.getUserId();
        logger.info("Show skills of user with ID {}", userId);
        return ResponseEntity.ok(skillService.findAllByUserId(userId));
    }

    @GetMapping("/admin/skills")
    @ApiOperation(value = "Show all skills", notes = "Show information about all skills")
    public ResponseEntity<List<SkillDetailsDTO>> getAllExtended() {
        logger.info("Show all skills");
        List<SkillDetailsDTO> skillsDto = skillService.findAll();
        return ResponseEntity.ok(skillsDto);
    }

    @GetMapping("/admin/skills/{id}")
    @ApiOperation(value = "Find skill by ID", notes = "Find skill if exists")
    public ResponseEntity<SkillDetailsDTO> findById(@ApiParam(value = "ID value for skill you need to retrieve", required = true)
                                                    @PathVariable UUID id) {
        logger.info("Find skill with ID = {}", id);
        SkillDetailsDTO skillDto = skillService.get(id);
        if (Objects.isNull(skillDto)) {
            logger.error("There is no skill with ID = {}", id);
        }
        return ResponseEntity.ok(skillDto);
    }

    @PostMapping("/admin/skills")
    @ApiOperation(value = "Add new skill", notes = "Add new skill")
    public ResponseEntity<SkillDetailsDTO> add(@RequestBody @Valid SkillDetailsDTO skillDto) {
        try {
        logger.info("Add skill with name {}", skillDto.getName());
        return ResponseEntity.ok(skillService.save(skillDto));
        } catch (Exception e){
            throw new NotUniqueEntityException("Such skill exists");
        }
    }

    @PutMapping("/admin/skills")
    @ApiOperation(value = "Update existing skill", notes = "Update existing skill")
    public ResponseEntity<SkillDetailsDTO> update(@RequestBody @Valid SkillDetailsDTO skill) {
        logger.info("Update skill with ID = {}", skill.getSkillId());
        return ResponseEntity.ok(skillService.update(skill));
    }
}
