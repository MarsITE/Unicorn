package com.academy.workSearch.dao.implementation;

import com.academy.workSearch.dao.RatingDAO;
import com.academy.workSearch.model.Rating;
import com.academy.workSearch.service.RatingService;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RatingDAOImpl extends CrudDAOImpl<Rating> implements RatingDAO {

    @Autowired
    public RatingDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
