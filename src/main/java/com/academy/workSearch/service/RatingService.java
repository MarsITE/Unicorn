package com.academy.workSearch.service;

import com.academy.workSearch.model.Rating;

import java.util.List;
import java.util.UUID;

public interface RatingService {

    List<Rating> findAll();

    Rating save(Rating rating);

    Rating get(UUID id);

    Rating delete(UUID id);

}
