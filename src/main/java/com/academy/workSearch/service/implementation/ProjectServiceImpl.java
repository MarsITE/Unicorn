package com.academy.workSearch.service.implementation;

import com.academy.workSearch.dao.ProjectDAO;
import com.academy.workSearch.dto.ProjectDTO;
import com.academy.workSearch.dto.mapper.ProjectMapper;
import com.academy.workSearch.model.Project;
import com.academy.workSearch.model.User;
import com.academy.workSearch.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectDAO projectDAO;

    @PostConstruct
    private void setTypeClass() {
        projectDAO.setClazz(Project.class);
    }

    @Override
    public List<ProjectDTO> findAll() {
        return ProjectMapper.INSTANCE.toProjectsDto(projectDAO.findAll());
    }

    @Override
    public List<ProjectDTO> findLast(int page, int maxResult, int maxNavigationPage, String sort) {
        return ProjectMapper.INSTANCE.toProjectsDto(projectDAO.findLast(page, maxResult, maxNavigationPage, sort));
    }

    @Override
    public ProjectDTO save(ProjectDTO projectDto) {
        User user = new User();
        user.setUserId(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f0"));

        Project project = ProjectMapper.INSTANCE.toEntity(projectDto);
        project.setEmployer(user);

        projectDAO.save(project);
        return projectDto;
    }

    @Override
    public Optional<ProjectDTO> get(UUID id) {
       Optional<Project> project = projectDAO.get(id);
       Optional<ProjectDTO> projectDTO = Optional.ofNullable(ProjectMapper.INSTANCE.toDto(project.get()));
        return projectDTO;
    }

    @Override
    public void delete(UUID id) {
        projectDAO.delete(id);
    }
}
