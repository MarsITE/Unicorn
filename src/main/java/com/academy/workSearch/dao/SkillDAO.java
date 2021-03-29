package com.academy.workSearch.dao;

import com.academy.workSearch.model.Skill;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class SkillDAO implements CrudDAO<Skill> {

    @Autowired
    private SessionFactory sessionFactory;
    private Session session = sessionFactory.getCurrentSession();

    @Override
    public List<Skill> findAll() {
        Session session = sessionFactory.getCurrentSession();
        Query<Skill> query = session.createQuery("from Skill", Skill.class);
        return query.getResultList();
    }

    @Override
    public void save(Skill skill) {
        session.saveOrUpdate(skill);
    }

    public void saveOnly(Skill skill) {
        session.save(skill);
    }

    @Override
    public Skill get(UUID id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Skill.class, id);
    }

    @Override
    public void delete(UUID id) {
        Session session = sessionFactory.getCurrentSession();
        Query<Skill> query = session.createQuery("delete from Skill where id =:skillId");
        query.setParameter("skillId", id);
        query.executeUpdate();
    }
}
