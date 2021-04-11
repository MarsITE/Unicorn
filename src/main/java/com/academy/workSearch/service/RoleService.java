package com.academy.workSearch.service;

import com.academy.workSearch.model.Role;

import java.util.List;
import java.util.UUID;

public interface RoleService {

    List<Role> findAll();

    Role save(Role role);

    Role get(UUID id);

    Role delete(UUID id);
}
