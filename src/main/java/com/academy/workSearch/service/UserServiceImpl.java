package com.academy.workSearch.service;

import com.academy.workSearch.dao.CrudDAO;
import com.academy.workSearch.dao.UserDAOImpl;
import com.academy.workSearch.dto.UserRegistrationDto;
import com.academy.workSearch.model.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.academy.workSearch.dto.mapper.UserRegistrationMapper.USER_REGISTRATION_MAPPER;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final CrudDAO<User> userCrudDAO;


    @Autowired

    private final UserDAOImpl userDAO;


    @Override
    public void save(UserRegistrationDto user) {
        user.setEmail(user.getEmail());
        user.setPassword(user.getPassword());
        userDAO.save(USER_REGISTRATION_MAPPER.toEntity(user));
    }

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

//    public Optional<User> findByEmail(String email) {
//        return userDAO.findByEmail(email);
//    }
}

