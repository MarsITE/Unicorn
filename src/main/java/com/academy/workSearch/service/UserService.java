package com.academy.workSearch.service;

import com.academy.workSearch.dto.UserAuthDTO;
import com.academy.workSearch.dto.UserDTO;
import com.academy.workSearch.dto.UserLoginDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> findAll();

    UserLoginDTO save(UserAuthDTO userAuthDTO);

    UserDTO update(UserDTO user);

    void deleteByEmail(String email);

    UserLoginDTO get(UserAuthDTO userAuth);

    UserDTO getByEmail(String email);
}
