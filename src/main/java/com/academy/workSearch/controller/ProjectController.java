package com.academy.workSearch.controller;

import com.academy.workSearch.dto.ProjectDTO;
import com.academy.workSearch.model.User;
import com.academy.workSearch.service.ProjectService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @ApiOperation(value = "Show projects", notes = "Show information about projects")
    public ResponseEntity<List<ProjectDTO>> showProjects(@RequestParam(value = "page", defaultValue = "1") int page,
                                                         @RequestParam(value = "sort", defaultValue = "desc") String sort,
                                                         @RequestParam(value = "maxResult", defaultValue = "5") int maxResult,
                                                         @RequestParam(value = "maxNavigationPage", defaultValue = "100") int maxNavigationPage,
                                                         @RequestParam(value = "showAll", defaultValue = "true") boolean showAll,
                                                         @AuthenticationPrincipal User currentUser) {

        List<ProjectDTO> projectsDto = projectService.findAllByPageWithSortOrder(page, maxResult, maxNavigationPage, sort, showAll, currentUser);
        logger.info("Show projects");
        return new ResponseEntity<>(projectsDto, HttpStatus.OK);
    }

    @GetMapping("/search")
    @ApiOperation(value = "Search project", notes = "Search project by skills")
    public ResponseEntity<List<ProjectDTO>> searchProjectBySkill(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                 @RequestParam(value = "sort", defaultValue = "desc") String sort,
                                                                 @RequestParam(value = "maxResult", defaultValue = "5") int maxResult,
                                                                 @RequestParam(value = "maxNavigationPage", defaultValue = "100") int maxNavigationPage,
                                                                 @RequestParam(value = "skillList") List<String> skills){
        List<ProjectDTO> projectsDto = projectService.searchBySkill(skills, page, maxResult, maxNavigationPage, sort);
        logger.info("Show projects with skills");
        logger.info(String.valueOf(skills));
        return new ResponseEntity<>(projectsDto, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    @ApiOperation(value = "Find project by ID", notes = "Find project if exists")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable UUID id) {
        ProjectDTO projectDto = projectService.get(id);
        logger.info("Find project with ID = {}", id);
        return new ResponseEntity<>(projectDto, HttpStatus.OK);
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
        logger.info("Update project with ID = {}", projectDto.getId());
        projectService.update(projectDto);
        return new ResponseEntity<>(projectDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete existing project", notes = "Delete existing project")
    public void deleteProject(@PathVariable UUID id) {
        projectService.delete(id);
        logger.info("Delete project with ID = {}", id);
    }
}
