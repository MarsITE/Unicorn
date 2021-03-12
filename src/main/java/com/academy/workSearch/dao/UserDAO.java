package com.academy.workSearch.dao;

import com.academy.workSearch.model.User;

import java.util.List;
import java.util.UUID;

public interface UserDAO {

    List<User> findAll();

    void saveUser(User user);

    User getUser(UUID id);

    void deleteUser(UUID id);
}
