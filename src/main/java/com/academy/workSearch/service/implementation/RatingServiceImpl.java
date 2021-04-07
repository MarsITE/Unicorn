package com.academy.workSearch.service.implementation;

import com.academy.workSearch.dao.CrudDAO;
import com.academy.workSearch.model.Rating;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class RatingServiceImpl {

    //    @Autowired
    private final CrudDAO<Rating> ratingDAO;

    public List<Rating> findAll() {
        return ratingDAO.findAll();
    }

    public Rating save(Rating rating) {
        return ratingDAO.save(rating);
    }

    public Optional<Rating> get(UUID id) {
        return ratingDAO.get(id);
    }

    public Rating delete(UUID id) {
        Rating rating = ratingDAO.get(id).orElseThrow();
        ratingDAO.delete(id);
        return rating;
    }
}
