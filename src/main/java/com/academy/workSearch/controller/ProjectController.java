package com.academy.workSearch.controller;

import com.academy.workSearch.dto.ProjectDTO;
import com.academy.workSearch.service.ProjectService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping()
    @ApiOperation(value = "Show projects", notes = "Show information about projects")
    public ResponseEntity<List<ProjectDTO>> showProjects(@RequestParam(value = "page", defaultValue = "1") int page,
                                                         @RequestParam(value = "sort", defaultValue = "desc") String sort,
                                                         @RequestParam(value = "maxResult", defaultValue = "5") int maxResult,
                                                         @RequestParam(value = "maxNavigationPage", defaultValue = "100") int maxNavigationPage) {
        List<ProjectDTO> projectsDto = projectService.findLast(page, maxResult, maxNavigationPage, sort);
        logger.info("Show projects");
        return new ResponseEntity<>(projectsDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Find project by ID", notes = "Find project in DB, if project exist")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable UUID id) {
        Optional<ProjectDTO> projectDto = projectService.get(id);
        logger.info("Find project with ID = {}", id);
        return new ResponseEntity<>(projectDto.get(), HttpStatus.OK);
    }

    @PostMapping()
    @ApiOperation(value = "Add new project", notes = "Add new project")
    public ResponseEntity<ProjectDTO> addNewProject(@RequestBody ProjectDTO projectDto) {
        logger.info("Add project with ID = {}", projectDto.getId());
        projectService.save(projectDto);
        return new ResponseEntity<>(projectDto, HttpStatus.OK);
    }

    @PutMapping()
    @ApiOperation(value = "Update existing project", notes = "Update existing project")
    public ResponseEntity<ProjectDTO> updateProject(@RequestBody ProjectDTO projectDto) {
        logger.info("Update project with ID = " + projectDto.getId());
        projectService.save(projectDto);
        return new ResponseEntity<>(projectDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete existing user", notes = "Delete existing project")
    public void deleteProject(@PathVariable UUID id) {
        projectService.delete(id);
        logger.info("Delete project with ID = " + id);
    }
}
