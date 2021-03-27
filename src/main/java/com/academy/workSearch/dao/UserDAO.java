package com.academy.workSearch.dao;

import com.academy.workSearch.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserDAO {
//    Optional<User> findByEmail(String email);
    User getByEmail(String email);

    UUID getIdByEmail(String email);

}
