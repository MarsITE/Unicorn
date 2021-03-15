package com.academy.workSearch.dao;

import com.academy.workSearch.model.Project;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class ProjectDAO implements CrudDAO<Project> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Project> findAll() {
        Session session = sessionFactory.getCurrentSession();
        Query<Project> query = session.createQuery("from Project", Project.class);
        return query.getResultList();
    }

    @Override
    public void save(Project project) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(project);
    }

    @Override
    public Project get(UUID id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Project.class, id);
    }

    @Override
    public void delete(UUID id) {
        Session session = sessionFactory.getCurrentSession();
        Query<Project> query = session.createQuery("delete from Project where id =:projectId");
        query.setParameter("projectId", id);
        query.executeUpdate();
    }
}
