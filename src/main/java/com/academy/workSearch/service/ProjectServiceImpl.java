package com.academy.workSearch.service;

import com.academy.workSearch.dao.CrudDAOImpl;
import com.academy.workSearch.dao.ProjectDAO;
import com.academy.workSearch.dto.ProjectDTO;
import com.academy.workSearch.dto.mapper.ProjectMapper;
import com.academy.workSearch.model.Project;
import com.academy.workSearch.model.ProjectStatus;
import com.academy.workSearch.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final CrudDAOImpl<Project> projectCrudDAO;
    private final ProjectDAO projectDAO;

    @Override
    public List<ProjectDTO> findAll() {
        projectCrudDAO.setClazz(Project.class);
        return ProjectMapper.INSTANCE.toProjectsDto(projectCrudDAO.findAll());
    }

    @Override
    public List<ProjectDTO> findLast(int page, int maxResult) {
        return ProjectMapper.INSTANCE.toProjectsDto(projectDAO.findLast(page, maxResult));
    }

    @Override
    public void save(ProjectDTO projectDto) {
        projectCrudDAO.setClazz(Project.class);

        User user = new User();
        user.setUserId(UUID.fromString("8486765d-0a3b-4dea-9c22-66746c440a22"));

        Project project = ProjectMapper.INSTANCE.toEntity(projectDto);
        project.setEmployer(user);
        project.setProjectStatus(ProjectStatus.LOOKING_FOR_WORKER);
        projectCrudDAO.save(project);
    }

    @Override
    public ProjectDTO get(UUID id) {
        return ProjectMapper.INSTANCE.toDto(projectCrudDAO.get(id));
    }

    @Override
    public void delete(UUID id) {
        projectCrudDAO.delete(id);
    }
}
