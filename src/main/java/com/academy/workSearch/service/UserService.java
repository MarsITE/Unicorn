package com.academy.workSearch.service;

import com.academy.workSearch.dto.UserDTO;

import javax.validation.ValidationException;
import java.util.List;
import java.util.UUID;

public interface UserService {

    List<UserDTO> findAll();

    void save(UserDTO UserDTO);

    UserDTO update(UserDTO user);

    UserDTO get(UUID id);

    void delete(UUID id);

    void deleteByEmail(String email);

    UserDTO getByEmail(String email);

    UUID getIdByEmail(String email);
}
