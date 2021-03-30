package com.academy.workSearch.dao.implementation;

import com.academy.workSearch.model.Rating;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RatingDAO extends CrudDAOImpl<Rating> {

    private final SessionFactory sessionFactory;

    @Autowired
    public RatingDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }
}
