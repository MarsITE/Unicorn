package com.academy.workSearch.service.implementation;

import com.academy.workSearch.dao.CrudDAO;
import com.academy.workSearch.exceptionHandling.exceptions.NoSuchEntityException;
import com.academy.workSearch.model.Rating;
import com.academy.workSearch.service.RatingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.academy.workSearch.exceptionHandling.MessageConstants.NO_RATING;

@Service
@Transactional
@AllArgsConstructor
public class RatingServiceImpl implements RatingService {


    private final CrudDAO<Rating> ratingDAO;

    public List<Rating> findAll() {
        return ratingDAO.findAll();
    }

    public Rating save(Rating rating) {
        return ratingDAO.save(rating);
    }

    public Rating get(UUID id) {
        return ratingDAO.get(id).orElseThrow(() -> new NoSuchEntityException(NO_RATING + id));

    }

    public Rating delete(UUID id) {
        Rating rating = ratingDAO.get(id)
                .orElseThrow(() -> new NoSuchEntityException(NO_RATING + id));
        ratingDAO.delete(id);
        return rating;
    }
}
