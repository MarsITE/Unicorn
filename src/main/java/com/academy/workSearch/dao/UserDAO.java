package com.academy.workSearch.dao;

import com.academy.workSearch.model.User;
import io.jsonwebtoken.ExpiredJwtException;

import java.util.Optional;

public interface UserDAO extends CrudDAO<User> {
    Optional<User> getByEmail(String email);

    User deleteByEmail(String email);

    boolean isValidRegistrationToken(String token);

}
