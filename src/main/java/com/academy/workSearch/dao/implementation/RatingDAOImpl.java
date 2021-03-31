package com.academy.workSearch.dao.implementation;

import com.academy.workSearch.model.Rating;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RatingDAOImpl extends CrudDAOImpl<Rating> {

    @Autowired
    public RatingDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
