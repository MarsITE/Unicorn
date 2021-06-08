package com.academy.workSearch.service;

import com.academy.workSearch.dto.ProjectDTO;
import com.academy.workSearch.dto.ProjectWorkerDTO;
import com.academy.workSearch.dto.WorkerProjectDTO;
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

    List<ProjectDTO> findUserProjectBySkills(UUID userId, int page, int maxResult, int maxNavigationPage, String sort);

    Long getAllProjectsCount();

    Long getAllProjectsCountByUserSkills(UUID userId);

    Long getAllProjectsCountBySearchSkills(List<String> searchSkills);

    ProjectDTO update(UUID id, ProjectDTO projectDTO);

    void joinProject(UUID projectId, User worker);

    List<WorkerProjectDTO> getWorkerProjects(User worker);

    List<WorkerProjectDTO> getWorkerProjects(UUID workerId);

    List<ProjectWorkerDTO> getProjectWorkers(UUID projectId);

    void updateApprovedWorker(UUID projectId, UUID projectUserInfoId);

    void deleteRequestedWorker(UUID projectId, UUID projectUserInfoId);
}
