package com.academy.workSearch.service;

import com.academy.workSearch.dto.ProjectDTO;

import java.util.List;
import java.util.UUID;

public interface ProjectService {

    List<ProjectDTO> findAll();

    List<ProjectDTO> findLast(int page, int maxResult, int maxNavigationPage, String sort);

    void save(ProjectDTO projectDto);

    ProjectDTO get(UUID id);

    void delete(UUID id);
}
