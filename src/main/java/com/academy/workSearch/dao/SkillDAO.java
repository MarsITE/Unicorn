package com.academy.workSearch.dao;

import com.academy.workSearch.model.Skill;

public interface SkillDAO {
    Skill getByName(String name);
}