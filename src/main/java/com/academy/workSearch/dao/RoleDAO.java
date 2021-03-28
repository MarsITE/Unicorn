package com.academy.workSearch.dao;

import com.academy.workSearch.model.Role;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDAO extends CrudDAOImpl<Role> {

    @Autowired
    private SessionFactory sessionFactory;

}
