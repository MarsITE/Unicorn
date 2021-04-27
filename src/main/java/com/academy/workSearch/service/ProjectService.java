package com.academy.workSearch.service;

import com.academy.workSearch.dto.ProjectDTO;
import com.academy.workSearch.dto.SkillDTO;
import com.academy.workSearch.model.User;

import java.util.List;
import java.util.UUID;

public interface ProjectService {

    List<ProjectDTO> findAll();

    List<ProjectDTO> findAllByPageWithSortOrder(int page, int maxResult, int maxNavigationPage, String sort, boolean showAll, User currentUser);

    List<ProjectDTO> findAllByOwnerId(int page, int maxResult, int maxNavigationPage, String sort, String ownerId);

    ProjectDTO save(ProjectDTO projectDto);

    ProjectDTO get(UUID id);

    ProjectDTO delete(UUID id);

    List<ProjectDTO> searchBySkill(List<String> skills, int page, int maxResult, int maxNavigationPage, String sort);

    ProjectDTO addSkillsToProject(ProjectDTO projectDto, SkillDTO skillDTO);
}
