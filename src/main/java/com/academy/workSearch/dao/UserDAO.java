package com.academy.workSearch.dao;

import com.academy.workSearch.model.User;

import java.util.UUID;

public interface UserDAO {
    User getByEmail(String email);

    UUID getIdByEmail(String email);

    void deleteByEmail(String email);
}
