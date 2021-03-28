package com.academy.workSearch.dao;

import com.academy.workSearch.model.Rating;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RatingDAO extends CrudDAOImpl<Rating> {

    @Autowired
    private SessionFactory sessionFactory;


}
