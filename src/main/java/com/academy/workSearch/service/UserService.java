package com.academy.workSearch.service;

import com.academy.workSearch.dto.UserRegistrationDTO;
import com.academy.workSearch.dto.UserDTO;
import com.academy.workSearch.dto.UserAuthDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDTO> findAll();

    UserAuthDTO save(UserRegistrationDTO userRegistrationDTO);

    UserDTO update(UserDTO user);

    UserDTO deleteByEmail(String email);

   Optional<UserAuthDTO> get(UserRegistrationDTO userAuth);

    Optional<UserDTO> getByEmail(String email);
}
