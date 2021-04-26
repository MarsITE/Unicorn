package com.academy.workSearch.dao;

import com.academy.workSearch.model.Project;
import java.util.List;

public interface ProjectDAO extends CrudDAO<Project> {
    List<Project> findAllByPageWithSortOrder(int page, int maxResult, int maxNavigationPage, String sort);

    boolean isPresentProjectByNameByUserId(String name, String id);
    List<Project> searchBySkill(List<String> skills, int page, int maxResult, int maxNavigationPage, String sort);
}
