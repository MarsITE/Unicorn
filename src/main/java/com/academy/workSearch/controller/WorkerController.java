package com.academy.workSearch.controller;

import com.academy.workSearch.dto.WorkerProjectDTO;
import com.academy.workSearch.model.User;
import com.academy.workSearch.service.ProjectService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/workers")
public class WorkerController {

    private final Logger logger = LoggerFactory.getLogger(WorkerController.class);

    private final ProjectService projectService;

    @GetMapping("/projects")
    @ApiOperation(value = "Get worker's projects", notes = "Update existing project")
    public ResponseEntity<List<WorkerProjectDTO>> getWorkerProjects(@AuthenticationPrincipal User worker) {
        logger.info("Get worker's projects with ID = {}", worker.getUserId());
        List<WorkerProjectDTO> result = projectService.getWorkerProjects(worker);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}/projects")
    @ApiOperation(value = "Get worker's projects", notes = "Update existing project")
    public ResponseEntity<List<WorkerProjectDTO>> getWorkerProjects(@PathVariable("id") UUID workerId) {
        logger.info("Get worker's projects with ID = {}", workerId);
        List<WorkerProjectDTO> result = projectService.getWorkerProjects(workerId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
