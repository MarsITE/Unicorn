package com.academy.workSearch.service;

import com.academy.workSearch.dto.UserAuthDTO;
import com.academy.workSearch.dto.UserDTO;
import com.academy.workSearch.dto.UserRegistrationDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> findAll();

    UserAuthDTO save(UserRegistrationDTO userRegistrationDTO);

    UserDTO update(UserDTO user);

    UserDTO deleteByEmail(String email);

    UserAuthDTO get(UserRegistrationDTO userAuth);

    UserDTO getByEmail(String email);

    boolean isPresentUserByEmail(String email);

    UserAuthDTO refreshToken(UserAuthDTO userAuthDTO);
}
