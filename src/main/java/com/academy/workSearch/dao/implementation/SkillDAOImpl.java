package com.academy.workSearch.dao.implementation;

import com.academy.workSearch.model.Skill;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SkillDAOImpl extends CrudDAOImpl<Skill> {

    @Autowired
    public SkillDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
