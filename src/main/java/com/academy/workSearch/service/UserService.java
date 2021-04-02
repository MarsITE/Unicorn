package com.academy.workSearch.service;

import com.academy.workSearch.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDTO> findAll();

    UserDTO save(UserDTO userDTO);

    UserDTO update(UserDTO user);

    UserDTO deleteByEmail(String email);

    Optional<UserDTO> getByEmail(String email);

}
