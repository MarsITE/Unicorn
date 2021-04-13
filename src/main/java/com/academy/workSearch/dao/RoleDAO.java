package com.academy.workSearch.dao;

import com.academy.workSearch.model.Role;

import java.util.Optional;

public interface RoleDAO extends CrudDAO<Role> {
   Optional<Role> getByName(String name);
}
