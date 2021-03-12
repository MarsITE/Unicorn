package com.academy.workSearch.service;

import com.academy.workSearch.dao.CrudDAO;
import com.academy.workSearch.model.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class UserService implements CrudService<User> {

    @Autowired
    private final CrudDAO<User> userDAO;

    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    public void save(User user) {
        userDAO.save(user);
    }

    @Override
    public User get(UUID id) {
        return userDAO.get(id);
    }

    @Override
    public void delete(UUID id) {
        userDAO.delete(id);
    }
}
