package com.academy.workSearch.dao;

import com.academy.workSearch.model.Role;

public interface RoleDAO extends CrudDAO<Role> {

    Role getByName(String name);
}
