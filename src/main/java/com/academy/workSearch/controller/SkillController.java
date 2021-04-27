package com.academy.workSearch.controller;

import com.academy.workSearch.dto.SkillDTO;
import com.academy.workSearch.dto.SkillDetailsDTO;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

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

    /**
     * @return all skills
     */
    @GetMapping("/skills")
    @ApiOperation(value = "Show all skills", notes = "Show information about all skills")
    public ResponseEntity<List<SkillDTO>> getAll() {
        logger.info("Show all skills");
        return ResponseEntity.ok(skillService.findAllEnabled(true));
    }

    /**
     * @return all skills #only for logged worker
     */
    @GetMapping("/worker/skills")
    @ApiOperation(value = "Show all skills", notes = "Show information about all skills")
    public ResponseEntity<List<SkillDTO>> getAllByUserID(@AuthenticationPrincipal User user) {
        UUID userId = user.getUserId();
        logger.info("Show skills of user with ID {}", userId);
        return ResponseEntity.ok(skillService.findAllByUserId(userId));
    }

    /**
     * @param skills List<SkillDetailsDTO>
     * @return saves new skills for approve by admin; returns list of skills
     */
    @PostMapping("/worker/skills")
    @ApiOperation(value = "Add new skill", notes = "Add new skill")
    public ResponseEntity<List<SkillDetailsDTO>> addNewWokerSkills(@RequestBody @Valid List<SkillDetailsDTO> skills) {
        logger.info("Attempt to add skills {}", skills);
        return ResponseEntity.ok(
                skills.stream().map(
                    skill -> skillService.isPresentSkillByName(skill.getName()) ?
                                skill : skillService.save(skill)
                ).collect(Collectors.toList())
        );
    }

    /**
     * @return all skills #only for admin
     */
    @GetMapping("/admin/skills")
    @ApiOperation(value = "Show all skills", notes = "Show information about all skills")
    public ResponseEntity<List<SkillDetailsDTO>> getAllExtended() {
        logger.info("Show all skills");
        List<SkillDetailsDTO> skillsDto = skillService.findAll();
        return ResponseEntity.ok(skillsDto);
    }

    /**
     * @param id skill
     * @return get skill
     */
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

    /**
     * @param skillDto SkillDetailsDTO
     * @return new saved skill
     */
    @PostMapping("/admin/skills")
    @ApiOperation(value = "Add new skill", notes = "Add new skill")
    public ResponseEntity<SkillDetailsDTO> add(@RequestBody @Valid SkillDetailsDTO skillDto) {
        logger.info("Add skill with name {}", skillDto.getName());
        return ResponseEntity.ok(skillService.save(skillDto));
    }

    /**
     * @param skill SkillDetailsDto
     * @return updated skill
     */
    @PutMapping("/admin/skills")
    @ApiOperation(value = "Update existing skill", notes = "Update existing skill")
    public ResponseEntity<SkillDetailsDTO> update(@RequestBody @Valid SkillDetailsDTO skill) {
        logger.info("Update skill with ID = {}", skill.getSkillId());
        return ResponseEntity.ok(skillService.update(skill));
    }

    /**
     * @param id of skill need to delete
     * @return void
     */
    @DeleteMapping("/admin/skills/{id}")
    @ApiOperation(value = "Delete existing skill", notes = "Delete existing skill")
    public ResponseEntity<SkillDetailsDTO> deleteSkill(@PathVariable UUID id) {
        logger.info("Delete skill with ID = {}", id);
        return ResponseEntity.ok(skillService.delete(id));
    }
}
