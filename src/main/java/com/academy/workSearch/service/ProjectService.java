package com.academy.workSearch.service;

import com.academy.workSearch.dto.ProjectDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectService {

    List<ProjectDTO> findAll();

    List<ProjectDTO> findLast(int page, int maxResult, int maxNavigationPage, String sort);

    ProjectDTO save(ProjectDTO projectDto);

    Optional<ProjectDTO> get(UUID id);

    void delete(UUID id);
}
