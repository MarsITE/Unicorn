package com.academy.workSearch.dao.implementation;

import com.academy.workSearch.model.Skill;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SkillDAO extends CrudDAOImpl<Skill> {

    @Autowired
    private SessionFactory sessionFactory;

}
