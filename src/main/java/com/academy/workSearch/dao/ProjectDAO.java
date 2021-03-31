package com.academy.workSearch.dao;

import com.academy.workSearch.model.Project;

import java.util.List;

public interface ProjectDAO extends CrudDAO<Project> {
    List<Project> findLast(int page, int maxResult, int maxNavigationPage, boolean sort);
}
