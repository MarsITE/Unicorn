package com.academy.workSearch.dao;

import com.academy.workSearch.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectDAO extends CrudDAO<Project> {
    Page<Project> findAllByPageWithSortOrder(Pageable pageable, String sort);

    Page<Project> findAllByOwnerId(Pageable pageable, String sort, String ownerId);

    Page<Project> findAllByWorkerId(Pageable pageable, String sort, String workerId);

    boolean isPresentProjectByNameByUserId(String name, String id);

    Page<Project> searchBySkill(List<String> skills, Pageable pageable, String sort);

    Long getAllProjectsCount();

    Long getAllProjectsCountBySkills(List<String> skills);
}
