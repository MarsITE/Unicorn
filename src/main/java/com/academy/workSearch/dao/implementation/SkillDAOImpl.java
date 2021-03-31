package com.academy.workSearch.dao.implementation;

import com.academy.workSearch.dao.SkillDAO;
import com.academy.workSearch.model.Skill;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;


@Repository
public class SkillDAOImpl extends CrudDAOImpl<Skill> implements SkillDAO {

    @Autowired
    public SkillDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);

    }

    @Override
    public List<Skill> findAll() {
        Session session = sessionFactory.getCurrentSession();
        Query<Skill> query = session.createQuery("from Skill", Skill.class);
        return query.getResultList();
    }

    @Override
    public void save(Skill skill) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(skill);
    }

    @Override
    public Skill get(UUID id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Skill.class, id);
    }

    public Skill getByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Skill where name = :name");
        query.setParameter("name", name);
        return (Skill) query.list().get(0);
    }

    public List<Skill> getAllEnabled(Boolean enabled) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Skill where enabled = :enabled");
        query.setParameter("enabled", enabled);
        return query.list();
    }

}
