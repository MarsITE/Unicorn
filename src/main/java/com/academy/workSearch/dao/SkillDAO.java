package com.academy.workSearch.dao;

import com.academy.workSearch.model.Skill;
import com.academy.workSearch.model.UserInfo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SkillDAO extends CrudDAO<Skill> {
    Optional<Skill> getByName(String name);
    List<Skill> getAllByEnabled(Boolean enabled);
    List<Skill> getAllByUserId(UUID userId);
}
