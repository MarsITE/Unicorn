package com.academy.workSearch.controller;

import com.academy.workSearch.dto.ProjectDTO;
import com.academy.workSearch.service.ProjectService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    @ApiOperation(value = "Show all projects", notes = "Show information about all projects in DB")
    public ResponseEntity<List<ProjectDTO>> showProjects() {
        List<ProjectDTO> projectsDto = projectService.findAll();
        logger.info("Show all projects");
        return new ResponseEntity<> (projectsDto, HttpStatus.OK);
    }

    @GetMapping("/last/{page}")
    @ApiOperation(value = "Show projects", notes = "Show information about projects from DB")
    public ResponseEntity<List<ProjectDTO>> showLastProjects(@PathVariable int page) {
        List<ProjectDTO> projectsDto = projectService.findLast(page, 5);
        logger.info("Show projects from page = " + page);
        return new ResponseEntity<> (projectsDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Find project by ID", notes = "Find project in DB, if project exist")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable UUID id) {
        ProjectDTO projectDto = projectService.get(id);
        logger.info("Find project with ID = " + id);
        return new ResponseEntity<> (projectDto, HttpStatus.OK);
    }

    @PostMapping()
    @ApiOperation(value = "Add new project", notes = "Add new project in DB")
    public ResponseEntity<ProjectDTO> addNewProject(@RequestBody ProjectDTO projectDto) {
        logger.info("Add project with ID = " + projectDto.getId());
        projectService.save(projectDto);
        return new ResponseEntity<> (projectDto, HttpStatus.OK);
    }

    @PutMapping()
    @ApiOperation(value = "Update existing project", notes = "Update existing project")
    public ResponseEntity<ProjectDTO> updateProject(@Validated @RequestBody ProjectDTO projectDto) {
        logger.info("Update project with ID = " + projectDto.getId());
        projectService.save(projectDto);
        return new ResponseEntity<> (projectDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete existing user", notes = "Delete existing project")
    public void deleteProject(@PathVariable UUID id) {
        projectService.delete(id);
        logger.info("Delete project with ID = " + id);
    }
}
