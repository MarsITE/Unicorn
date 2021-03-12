package com.academy.workSearch.dao;

import com.academy.workSearch.model.User;

import java.util.List;
import java.util.UUID;

public interface UserDAO {

    public List<User> findAll();

    public void saveUser(User user);

    public User getUser(UUID id);

    public void deleteUser(UUID id);
}
