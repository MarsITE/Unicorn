package com.academy.workSearch.service;

import com.academy.workSearch.dto.UserAuthDTO;
import com.academy.workSearch.dto.UserDTO;
import com.academy.workSearch.dto.UserRegistrationDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {

    List<UserDTO> findAll();

    UserAuthDTO save(UserRegistrationDTO userRegistrationDTO);

    UserDTO update(UserDTO user);

    UserDTO delete(UUID id);

    UserDTO get(UUID id);

    UserAuthDTO get(UserRegistrationDTO userAuth);

    boolean isPresentUserByEmail(String email);

    UserAuthDTO refreshToken(UserAuthDTO userAuthDTO);

    boolean isVerifyAccount(String token);

    void removeAllNotActiveUsersWithExpiredJWTToken();

    UserDTO makeAdmin(UUID id);
}
