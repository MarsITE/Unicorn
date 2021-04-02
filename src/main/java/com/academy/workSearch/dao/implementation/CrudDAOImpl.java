package com.academy.workSearch.dao.implementation;

import com.academy.workSearch.dao.CrudDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CrudDAOImpl<E> implements CrudDAO<E> {
    protected SessionFactory sessionFactory;
    private Class<E> clazz;

    @Autowired
    public CrudDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void setClazz(Class<E> clazzToSet) {
        this.clazz = clazzToSet;
    }

    @Override
    public List<E> findAll() {
        Session session = sessionFactory.getCurrentSession();
        Query<E> query = session.createQuery("from " + clazz.getName(), clazz);
        return query.getResultList();
    }

    @Override
    public E save(E entity) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(entity);
        return entity;
    }

    @Override
    public Optional<E> get(UUID id) {
        Session session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(session.get(clazz, id));
    }

    @Override
    public Optional<E> delete(UUID id) {
        Session session = sessionFactory.getCurrentSession();
        E entity = session.get(clazz, id);
        if (entity != null) {
            session.delete(entity);
        }
        return Optional.ofNullable(entity);
    }
}
