package com.academy.workSearch.dao;

import com.academy.workSearch.model.Skill;

import java.util.List;
import java.util.Optional;

public interface SkillDAO extends CrudDAO<Skill> {
   Optional<Skill> getByName(String name);
    List<Skill> getAllEnabled(Boolean enabled);
}
