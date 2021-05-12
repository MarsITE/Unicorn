package com.academy.workSearch.service.implementation;

import com.academy.workSearch.dao.ProjectDAO;
import com.academy.workSearch.dao.RoleDAO;
import com.academy.workSearch.dao.implementation.UserDAOImpl;
import com.academy.workSearch.dto.*;
import com.academy.workSearch.dto.mapper.ProjectMapper;
import com.academy.workSearch.dto.mapper.ProjectShowInfoMapper;
import com.academy.workSearch.dto.mapper.SkillMapper;
import com.academy.workSearch.exceptionHandling.exceptions.NoSuchEntityException;
import com.academy.workSearch.exceptionHandling.exceptions.NotUniqueEntityException;
import com.academy.workSearch.model.Project;
import com.academy.workSearch.model.ProjectUserInfo;
import com.academy.workSearch.model.Role;
import com.academy.workSearch.model.Skill;
import com.academy.workSearch.model.User;
import com.academy.workSearch.model.UserInfo;
import com.academy.workSearch.model.enums.ProjectStatus;
import com.academy.workSearch.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

import static com.academy.workSearch.exceptionHandling.MessageConstants.NOT_UNIQUE_PROJECT;
import static com.academy.workSearch.exceptionHandling.MessageConstants.NO_PROJECT;

@Service
@Transactional
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectDAO projectDAO;
    private final UserDAOImpl userDAO;
    private final RoleDAO roleDAO;
    private final SkillServiceImpl skillService;

    @PostConstruct
    private void setTypeClass() {
        userDAO.setClazz(User.class);
        projectDAO.setClazz(Project.class);
    }

    /**
     * @return list of all projects
     */
    @Override
    public List<ProjectDTO> findAll() {
        return ProjectMapper.INSTANCE.toProjectsDto(projectDAO.findAll());
    }

    /**
     * @param page              number of page
     * @param maxResult         count of projects in current page
     * @param maxNavigationPage number of last page
     * @param sort              asc or desc
     * @param showAll           boolean
     * @param currentUser       user
     * @return list of projects
     */
    @Override
    public List<ProjectDTO> findAllByPageWithSortOrder(int page, int maxResult, int maxNavigationPage, String sort, boolean showAll, User currentUser) {
        boolean showOnlyOwnedProjects = false;
        if (!showAll && currentUser != null && !currentUser.getRoles().isEmpty()) {
            Set<Role> roles = currentUser.getRoles();
            String employerRoleName = roleDAO.getByName("EMPLOYER").orElseThrow().getName();
            String workerRoleName = roleDAO.getByName("WORKER").orElseThrow().getName();
            for (Role role : roles) {
                if (role.getName().equals(employerRoleName) || role.getName().equals(workerRoleName)) {
                    showOnlyOwnedProjects = true;
                    break;
                }
            }
        }
        List<Project> allByPageWithSortOrder;
        if (showOnlyOwnedProjects) {
            allByPageWithSortOrder = projectDAO.findAllByOwnerId(page, maxResult, maxNavigationPage, sort, currentUser.getUserId().toString());
        } else {
            allByPageWithSortOrder = projectDAO.findAllByPageWithSortOrder(page, maxResult, maxNavigationPage, sort);
        }
        return ProjectMapper.INSTANCE.toProjectsDto(allByPageWithSortOrder);
    }

    /**
     * @param page              number of current page
     * @param maxResult         count of projects in current page
     * @param maxNavigationPage number of last page
     * @param sort              asc or desc
     * @param ownerId           id of current user
     * @return list of projects for current user
     */
    @Override
    public List<ProjectDTO> findAllByOwnerId(int page, int maxResult, int maxNavigationPage, String sort, String ownerId) {
        return ProjectMapper.INSTANCE.toProjectsDto(projectDAO.findAllByOwnerId(page, maxResult, maxNavigationPage, sort, ownerId));
    }

    /**
     * @param skills            list of skills
     * @param page              number of current page
     * @param maxResult         count of projects in current page
     * @param maxNavigationPage number of last page
     * @param sort              asc or desc
     * @return list of projects for current skills
     */
    @Override
    public List<ProjectDTO> searchBySkill(List<String> skills, int page, int maxResult, int maxNavigationPage, String sort) {
        return ProjectMapper.INSTANCE.toProjectsDto(projectDAO.searchBySkill(skills, page, maxResult, maxNavigationPage, sort));
    }

    @Override
    public List<ProjectDTO> findUserProjectBySkills(UUID userId, int page, int maxResult, int maxNavigationPage, String sort) {
        List<String> userSkills = skillService.findAllByUserId(userId).stream().map(SkillDetailsDTO::getName).collect(Collectors.toList());
        return ProjectMapper.INSTANCE.toProjectsDto(projectDAO.searchBySkill(userSkills, page, maxResult, maxNavigationPage, sort));
    }

    /**
     * @param projectDto info of current project
     * @return projectDto
     */
    @Override
    public ProjectDTO save(ProjectDTO projectDto) {
        if (projectDAO.isPresentProjectByNameByUserId(projectDto.getName(), projectDto.getOwnerId())) {
            throw new NotUniqueEntityException(NOT_UNIQUE_PROJECT + projectDto.getId());
        }
        User user = new User();
        user.setUserId(UUID.fromString(projectDto.getOwnerId()));
        Project project = new Project();
        project.setName(projectDto.getName());
        project.setDescription(projectDto.getDescription());
        project.setEmployer(user);
        project.setSkills(SkillMapper.SKILL_MAPPER.toSkills(projectDto.getSkills()));
        project.setProjectStatus(ProjectStatus.valueOf(projectDto.getProjectStatus()));
        projectDAO.save(project);
        return projectDto;
    }

    @Override
    public ProjectDTO update(UUID id, ProjectDTO projectDTO) {
        Project oldProject = projectDAO.get(id)
                .orElseThrow(() -> new NoSuchEntityException(NO_PROJECT + id));
        Project newProject = ProjectMapper.INSTANCE.toEntity(projectDTO);
        oldProject.setName(newProject.getName());
        oldProject.setDescription(newProject.getDescription());
        oldProject.setProjectStatus(newProject.getProjectStatus());
        oldProject.setSkills(newProject.getSkills());
        projectDAO.save(oldProject);
        return ProjectMapper.INSTANCE.toDto(oldProject);
    }

    @Override
    public void joinProject(UUID projectId, User worker) {
        Project project = projectDAO.get(projectId)
                .orElseThrow(() -> new NoSuchEntityException(NO_PROJECT + projectId));
        UserInfo userInfo = userDAO.get(worker.getUserId())
                .map(User::getUserInfo)
                .orElseThrow();
        project.addUserInfo(userInfo);
    }

    @Override
    public List<WorkerProjectDTO> getWorkerProjects(User worker) {
        List<WorkerProjectDTO> result = new ArrayList<>();
        User user = userDAO.get(worker.getUserId()).orElseThrow();
        for (ProjectUserInfo projectUserInfo : user.getUserInfo().getProjects()) {
            WorkerProjectDTO workerProjectDTO = ProjectShowInfoMapper.INSTANCE.toWorkerDto(projectUserInfo.getProject());
            workerProjectDTO.setApprove(projectUserInfo.isApprove());
            result.add(workerProjectDTO);
        }
        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProjectWorkerDTO> getProjectWorkers(UUID projectId) {
        List<ProjectWorkerDTO> res = new ArrayList<>();
        Project project = projectDAO.get(projectId).orElseThrow();
        for (ProjectUserInfo worker : project.getWorkers()) {
            ProjectWorkerDTO projectWorkerDTO = ProjectMapper.INSTANCE.toProjectWorkerDTO(worker);
            res.add(projectWorkerDTO);
        }
        return res;
    }

    @Transactional
    @Override
    public void updateApprovedWorker(UUID projectId, UUID projectUserInfoId) {
        Project project = projectDAO.get(projectId).orElseThrow();
        for (ProjectUserInfo worker : project.getWorkers()) {
            if (worker.getUserInfoProjectId().equals(projectUserInfoId)) {
                worker.setApprove(!worker.isApprove());
                break;
            }
        }
        projectDAO.save(project);
    }

    @Transactional
    @Override
    public void deleteRequestedWorker(UUID projectId, UUID projectUserInfoId) {
        Project project = projectDAO.get(projectId).orElseThrow();
        for (ProjectUserInfo worker : project.getWorkers()) {
            if (worker.getUserInfoProjectId().equals(projectUserInfoId)) {
                project.removeUserInfo(worker.getUserInfo());
                break;
            }
        }
        projectDAO.save(project);
    }

    @Override
    public Long getAllProjectsCount() {
        return projectDAO.getAllProjectsCount();
    }

    @Override
    public Long getAllProjectsCountByUserSkills(UUID userId) {
        List<String> userSkillNames = skillService.findAllByUserId(userId).stream().map(SkillDetailsDTO::getName).collect(Collectors.toList());
        return projectDAO.getAllProjectsCountBySkills(userSkillNames);
    }

    @Override
    public Long getAllProjectsCountBySearchSkills(List<String> searchSkills) {
        return projectDAO.getAllProjectsCountBySkills(searchSkills);
    }

    /**
     * @param id id of current project
     * @return projectDto
     */
    @Override
    public ProjectDTO get(UUID id) {
        Project project = projectDAO.get(id)
                .orElseThrow(() -> new NoSuchEntityException(NO_PROJECT + id));
        return ProjectMapper.INSTANCE.toDto(project);

    }

    /**
     * @param id id of current project
     * @return projectDto
     */
    @Override
    public ProjectDTO delete(UUID id) {
        Project project = projectDAO.get(id)
                .orElseThrow(() -> new NoSuchEntityException(NO_PROJECT + id));
        projectDAO.delete(id);
        return ProjectMapper.INSTANCE.toDto(project);
    }
}
