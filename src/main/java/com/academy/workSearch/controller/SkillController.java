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
    public ResponseEntity<List<SkillDetailsDTO>> getAllByUserID(@AuthenticationPrincipal User user) {
        UUID userId = user.getUserId();
        logger.info("Show skills of user with ID {}", userId);
        return ResponseEntity.ok(skillService.findAllByUserId(userId));
    }

    /**
     * @param skills that will be added to worker
     * @return saves new skills for approve by admin; returns list of skills
     */
    @PostMapping("/worker/skills")
    @ApiOperation(value = "Add new skill", notes = "Add new skill")
    public ResponseEntity<List<SkillDetailsDTO>> addSkillsForWorker(@RequestBody @Valid List<SkillDetailsDTO> skills,
                                                                    @AuthenticationPrincipal User user) {
        logger.info("Attempt to add skills {}", skills);
        List<SkillDetailsDTO> addedSkills = skillService.saveSkillList(skills);

        skillService.sendEmail(user.getEmail(), addedSkills);

        UUID userInfoId = user.getUserInfo().getUserInfoId();
        skillService.saveWorkerSkills(addedSkills, userInfoId);
        return ResponseEntity.ok(addedSkills);
    }

    /**
     * @param skillId of worker's skill need to delete
     * @return void
     */
    @DeleteMapping("/worker/skills/{skillId}")
    @ApiOperation(value = "Delete worker's skill", notes = "Delete worker's skill")
    public ResponseEntity<SkillDetailsDTO> deleteWorkerSkill(@ApiParam(value = "ID value for worker's skill you need to delete", required = true)
                                                             @PathVariable UUID skillId, @AuthenticationPrincipal User user) {
        UUID userInfoId = user.getUserInfo().getUserInfoId();
        logger.info("Delete skill of worker: workerId= {}, skillId = {}", user.getUserId(), skillId);
        return ResponseEntity.ok(skillService.deleteByUserInfoIdBySkillId(userInfoId, skillId));
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
