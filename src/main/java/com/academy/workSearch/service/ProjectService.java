package com.academy.workSearch.service;

import com.academy.workSearch.dao.CrudDAO;
import com.academy.workSearch.model.Project;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class ProjectService implements CrudService<Project> {

    private final CrudDAO<Project> projectDAO;

    @Override
    public List<Project> findAll() {
        return projectDAO.findAll();
    }

    @Override
    public void save(Project project) {
        projectDAO.save(project);
    }

    @Override
    public Project get(UUID id) {
        return projectDAO.get(id);
    }

    @Override
    public void delete(UUID id) {
        projectDAO.delete(id);
    }
}
