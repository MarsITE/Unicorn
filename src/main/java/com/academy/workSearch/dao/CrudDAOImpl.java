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
public class CrudDAOImpl<E> implements CrudDAO<E>{
    private Class<E> clazz;

    @Autowired
    private SessionFactory sessionFactory;

    public void setClazz(Class<E> clazzToSet){
        this.clazz = clazzToSet;
    }

    @Override
    public List<E> findAll() {
        Session session = sessionFactory.getCurrentSession();
        Query<E> query = session.createQuery("from " + clazz.getName(), clazz);
        return query.getResultList();
    }

    @Override
    public void save(E entity) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(entity);
    }

    @Override
    public E get(UUID id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(clazz, id);
    }

    @Override
    public void delete(UUID id) {
        Session session = sessionFactory.getCurrentSession();
        Query<Project> query = session.createQuery("delete from " + clazz.getName()
                + " where id=:entityId");
        query.setParameter("entityId", id);
        query.executeUpdate();
    }
}
