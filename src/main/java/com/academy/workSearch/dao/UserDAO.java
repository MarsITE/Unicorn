package com.academy.workSearch.dao;

import com.academy.workSearch.model.User;

public interface UserDAO extends CrudDAO<User> {
    User getByEmail(String email);

    void deleteByEmail(String email);

}
