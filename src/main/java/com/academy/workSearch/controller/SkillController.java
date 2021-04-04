package com.academy.workSearch.controller;

import com.academy.workSearch.dto.SkillDTO;
import com.academy.workSearch.exceptionHandling.NoSuchSkillException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/skills")
public class SkillController {
    private final Logger logger = LoggerFactory.getLogger(AdminSkillController.class);
    private SkillService skillService;

    @Autowired
    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping("")
    @ApiOperation(value = "Show all skills", notes = "Show information about all skills")
    public ResponseEntity<List<SkillDTO>> getAll() {
        logger.info("Show all skills");
        return ResponseEntity.ok(skillService.findAllEnabled(true));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Find skill by ID", notes = "Find skill if exists")
    public ResponseEntity<SkillDTO> findById(@ApiParam(value = "ID value for skill you need to retrieve", required = true)
                                             @PathVariable UUID id) {
        logger.info("Find skill with ID = {}", id);
        SkillDTO skillDto = skillService.get(id).get();
        if (Objects.isNull(skillDto)) {
            logger.error("There is no skill with ID = {}", id );
            throw new NoSuchSkillException("There is no skill with ID = " + id );
        }
        return ResponseEntity.ok(skillDto);
    }
}
