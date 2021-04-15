package com.academy.workSearch.dao;

import com.academy.workSearch.dto.UserAuthDTO;
import com.academy.workSearch.model.User;

import java.util.Optional;

public interface UserDAO extends CrudDAO<User> {
   Optional<User> getByEmail(String email);

   User deleteByEmail(String email);
}
