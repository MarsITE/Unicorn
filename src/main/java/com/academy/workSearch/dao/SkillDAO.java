package com.academy.workSearch.dao;

import com.academy.workSearch.model.Skill;

import java.util.List;

public interface SkillDAO extends CrudDAO<Skill> {
    Skill getByName(String name);
    List<Skill> getAllEnabled(Boolean enabled);
}
