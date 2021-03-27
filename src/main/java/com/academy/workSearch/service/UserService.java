package com.academy.workSearch.service;

import com.academy.workSearch.dto.UserRegistrationDto;
import com.academy.workSearch.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

//    Optional<User> findByEmail(String email);

    List<User> findAll();

    void save(User user);

    User get(UUID id);

    void delete(UUID id);

    void save(UserRegistrationDto user);
}



