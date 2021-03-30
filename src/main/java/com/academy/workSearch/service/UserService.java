package com.academy.workSearch.service;

import com.academy.workSearch.dto.UserAuthDTO;
import com.academy.workSearch.dto.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> findAll();

    UserAuthDTO save(UserAuthDTO userAuthDTO);

    UserDTO update(UserDTO user);

    void deleteByEmail(String email);

    UserAuthDTO get(String email, String password);
}
