package com.academy.workSearch.service;

import com.academy.workSearch.dao.CrudDAO;
import com.academy.workSearch.model.Rating;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class RatingService implements CrudService<Rating> {
    @Autowired
    private final CrudDAO<Rating> ratingDAO;

    @Override
    public List<Rating> findAll() {
        return ratingDAO.findAll();
    }

    @Override
    public void save(Rating rating) {
        ratingDAO.save(rating);
    }

    @Override
    public Rating get(UUID id) {
        return ratingDAO.get(id);
    }

    @Override
    public void delete(UUID id) {
        ratingDAO.delete(id);
    }
}
