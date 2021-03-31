package com.academy.workSearch.dao.implementation;

import com.academy.workSearch.model.Role;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDAOImpl extends CrudDAOImpl<Role> {

    @Autowired
    public RoleDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
