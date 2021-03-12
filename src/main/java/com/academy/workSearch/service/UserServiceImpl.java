package com.academy.workSearch.service;

import com.academy.workSearch.dao.UserDAO;
import com.academy.workSearch.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    private UserDAO userDAO;

    @Autowired
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    @Transactional
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        userDAO.saveUser(user);
    }

    @Override
    @Transactional
    public User getUser(UUID id) {
        return userDAO.getUser(id);
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        userDAO.deleteUser(id);
    }
}
