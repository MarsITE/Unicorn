package com.academy.workSearch.service;

import com.academy.workSearch.dao.CrudDAO;
import com.academy.workSearch.dao.CrudDAOImpl;
import com.academy.workSearch.dao.UserDAO;
import com.academy.workSearch.model.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    @Autowired
    private final UserDAO userDAO;

    public List<User> findAll() {
        return userDAO.findAll();
    }

    public void save(User user) {
        userDAO.save(user);
    }

    public User get(UUID id) {
        return userDAO.get(id);
    }

    public void delete(UUID id) {
        userDAO.delete(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    }

