package com.academy.workSearch.dao;

import com.academy.workSearch.model.Project;

import java.util.List;

public interface ProjectDAO {
    List<Project> findLast(int page, int maxResult);
}
