package com.academy.workSearch.service.implementation;

import com.academy.workSearch.dao.ProjectDAO;
import com.academy.workSearch.dao.RoleDAO;
import com.academy.workSearch.dao.implementation.UserDAOImpl;
import com.academy.workSearch.dto.ProjectDTO;
import com.academy.workSearch.dto.SkillDTO;
import com.academy.workSearch.dto.mapper.ProjectMapper;
import com.academy.workSearch.dto.mapper.SkillMapper;
import com.academy.workSearch.exceptionHandling.exceptions.NoSuchEntityException;
import com.academy.workSearch.exceptionHandling.exceptions.NotUniqueEntityException;
import com.academy.workSearch.model.Project;
import com.academy.workSearch.model.Role;
import com.academy.workSearch.model.Skill;
import com.academy.workSearch.model.User;
import com.academy.workSearch.model.enums.ProjectStatus;
import com.academy.workSearch.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.academy.workSearch.exceptionHandling.MessageConstants.NOT_UNIQUE_PROJECT;
import static com.academy.workSearch.exceptionHandling.MessageConstants.NO_PROJECT;

@Service
@Transactional
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectDAO projectDAO;
    private final UserDAOImpl userDAO;
    private final RoleDAO roleDAO;

    @PostConstruct
    private void setTypeClass() {
        userDAO.setClazz(User.class);
        projectDAO.setClazz(Project.class);
    }

    /**
     *
     * @return list of all projects
     */
    @Override
    public List<ProjectDTO> findAll() {
        return ProjectMapper.INSTANCE.toProjectsDto(projectDAO.findAll());
    }

    /**
     *
     * @param page  number of page
     * @param maxResult count of projects in current page
     * @param maxNavigationPage number of last page
     * @param sort  asc or desc
     * @param showAll   boolean
     * @param currentUser user
     * @return  list of projects
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
     *
     * @param page number of current page
     * @param maxResult count of projects in current page
     * @param maxNavigationPage number of last page
     * @param sort  asc or desc
     * @param ownerId id of current user
     * @return list of projects for current user
     */
    @Override
    public List<ProjectDTO> findAllByOwnerId(int page, int maxResult, int maxNavigationPage, String sort, String ownerId) {
        return ProjectMapper.INSTANCE.toProjectsDto(projectDAO.findAllByOwnerId(page, maxResult, maxNavigationPage, sort, ownerId));
    }

    /**
     *
     * @param skills list of skills
     * @param page number of current page
     * @param maxResult count of projects in current page
     * @param maxNavigationPage number of last page
     * @param sort asc or desc
     * @return list of projects for current skills
     */
    @Override
    public List<ProjectDTO> searchBySkill(List<String> skills, int page, int maxResult, int maxNavigationPage, String sort) {
        List<ProjectDTO> sds = ProjectMapper.INSTANCE.toProjectsDto(projectDAO.searchBySkill(skills, page, maxResult, maxNavigationPage, sort));
        return sds;
    }

    /**
     *
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
        project.setProjectStatus(ProjectStatus.valueOf(projectDto.getProjectStatus()));
        projectDAO.save(project);
        return projectDto;
    }

    /**
     *
     * @param projectDto current project
     * @param skillDTO  skills that we need to add
     * @return  projectDto
     */
    @Override
    public ProjectDTO addSkillsToProject(ProjectDTO projectDto, SkillDTO skillDTO) {
        Project project = ProjectMapper.INSTANCE.toEntity(projectDto);
        Skill skill = SkillMapper.SKILL_MAPPER.toEntity(skillDTO);
        Set<Skill> skills = new HashSet<>();
        skills.add(skill);
        project.setSkills(skills);
        projectDAO.save(project);
        return projectDto;
    }

    @Override
    public ProjectDTO update(ProjectDTO projectDTO) {
        Project oldProject = projectDAO.get(projectDTO.getId())
                .orElseThrow(() -> new NoSuchEntityException(NO_PROJECT + projectDTO.getId()));
        Project newProject = ProjectMapper.INSTANCE.toEntity(projectDTO);
        oldProject.setName(newProject.getName());
        oldProject.setDescription(newProject.getDescription());
        oldProject.setProjectStatus(newProject.getProjectStatus());
        projectDAO.save(oldProject);
        return ProjectMapper.INSTANCE.toDto(oldProject);
    }

    /**
     *
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
     *
     * @param id id of current project
     * @return  projectDto
     */
    @Override
    public ProjectDTO delete(UUID id) {
        Project project = projectDAO.get(id)
                .orElseThrow(() -> new NoSuchEntityException(NO_PROJECT + id));
        projectDAO.delete(id);
        return ProjectMapper.INSTANCE.toDto(project);
    }
}
