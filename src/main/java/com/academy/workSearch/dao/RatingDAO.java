package com.academy.workSearch.dao;

import com.academy.workSearch.model.Rating;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class RatingDAO implements CrudDAO<Rating> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Rating> findAll() {
        Session session = sessionFactory.getCurrentSession();
        Query<Rating> query = session.createQuery("from Rating", Rating.class);
        return query.getResultList();
    }

    @Override
    public void save(Rating rating) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(rating);
    }

    @Override
    public Rating get(UUID id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Rating.class, id);
    }

    @Override
    public void delete(UUID id) {
        Session session = sessionFactory.getCurrentSession();
        Query<Rating> query = session.createQuery("delete from Rating where id =:raitingId");
        query.setParameter("raitingId", id);
        query.executeUpdate();
    }
}
