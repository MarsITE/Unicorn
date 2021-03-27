package com.academy.workSearch.service;

import com.academy.workSearch.dao.CrudDAO;
import com.academy.workSearch.dao.ProjectDAOImpl;
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

    private final ProjectDAOImpl projectDAO;
    private final CrudDAO<User> userDAO;

    @Override
    public List<ProjectDTO> findAll() {
        return ProjectMapper.INSTANCE.toProjectsDto(projectDAO.findAll());
    }

    @Override
    public List<ProjectDTO> findLast(int page, int maxResult) {
        return ProjectMapper.INSTANCE.toProjectsDto(projectDAO.findLast(page, maxResult));
    }

    @Override
    public void save(ProjectDTO projectDto) {
        Project project = ProjectMapper.INSTANCE.toEntity(projectDto);
        project.setEmployer(userDAO.get(UUID.fromString("8486765d-0a3b-4dea-9c22-66746c440a22")));
        project.setProjectStatus(ProjectStatus.LOOKING_FOR_WORKER);
        projectDAO.save(project);
    }

    @Override
    public ProjectDTO get(UUID id) {
        return ProjectMapper.INSTANCE.toDto(projectDAO.get(id));
    }

    @Override
    public void delete(UUID id) {
        projectDAO.delete(id);
    }
}
