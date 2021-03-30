package com.academy.workSearch.service.implementation;

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
public class RatingService {

    @Autowired
    private final CrudDAO<Rating> ratingDAO;

    public List<Rating> findAll() {
        return ratingDAO.findAll();
    }

    public void save(Rating rating) {
        ratingDAO.save(rating);
    }

    public Rating get(UUID id) {
        return ratingDAO.get(id);
    }

    public void delete(UUID id) {
        ratingDAO.delete(id);
    }
}
