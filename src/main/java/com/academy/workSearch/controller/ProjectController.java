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
                                                                 @RequestParam(value = "skillList") List<String> skills) {
        List<ProjectDTO> projectsDto = projectService.searchBySkill(skills, page, maxResult, maxNavigationPage, sort);
        logger.info("Show projects with skills");
        logger.info(String.valueOf(skills));
        return new ResponseEntity<>(projectsDto, HttpStatus.OK);
    }
    @GetMapping("/worker")
    @ApiOperation(value = "Find projects by user ID and user skills", notes = "Return all projects user skills")
    public ResponseEntity<List<ProjectDTO>> findUserProjectBySkills(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                    @RequestParam(value = "sort", defaultValue = "desc") String sort,
                                                                    @RequestParam(value = "maxResult", defaultValue = "5") int maxResult,
                                                                    @RequestParam(value = "maxNavigationPage", defaultValue = "100") int maxNavigationPage,
                                                                    @AuthenticationPrincipal User user){
        UUID userId = user.getUserId();
        List<ProjectDTO> projectDto = projectService.findUserProjectBySkills(userId, page, maxResult, maxNavigationPage, sort);
        logger.info("Show projects by user skills");
        return new ResponseEntity<>(projectDto, HttpStatus.OK);
    }

    @GetMapping("/count/all")
    @ApiOperation(value = "count all projects", notes = "Return long as count of all projects")
    public ResponseEntity<Long> getCountOfAllProjects(){
        Long count = projectService.getAllProjectsCount();
        logger.info(count.toString());
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/count/user/skills")
    @ApiOperation(value = "count all projects by skills", notes = "Return long as count of all projects by user skills")
    public ResponseEntity<Long> getCountOfAllProjectsByUserSkills(@AuthenticationPrincipal User user){
        UUID userId = user.getUserId();
        Long count = projectService.getAllProjectsCountByUserSkills(userId);
        logger.info(count.toString());
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/count/search")
    @ApiOperation(value = "count all projects by search skills", notes = "Return long as count of all projects by search skills")
    public ResponseEntity<Long> getCountOfAllProjectsBySearchSkills(@RequestParam(value = "skillList") List<String> skills){
        Long count = projectService.getAllProjectsCountBySearchSkills(skills);
        return new ResponseEntity<>(count, HttpStatus.OK);
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

    @PutMapping("/{id}")
    @ApiOperation(value = "Update existing project", notes = "Update existing project")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable UUID id, @RequestBody ProjectDTO projectDto) {
        logger.info("Update project with ID = {}", id);
        projectService.update(id, projectDto);
        return new ResponseEntity<>(projectDto, HttpStatus.OK);
    }

    @PostMapping("/{id}/workers")
    @ApiOperation(value = "Update existing project", notes = "Update existing project")
    public void updateProject(@PathVariable("id") UUID projectId,
                                                    @AuthenticationPrincipal User worker) {
        logger.info("Join worker {} to project with ID = {}", worker.getUserId(), projectId);
        projectService.joinProject(projectId, worker);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete existing project", notes = "Delete existing project")
    public void deleteProject(@PathVariable UUID id) {
        projectService.delete(id);
        logger.info("Delete project with ID = {}", id);
    }
}
