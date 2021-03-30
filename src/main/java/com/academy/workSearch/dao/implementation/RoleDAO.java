package com.academy.workSearch.dao.implementation;

import com.academy.workSearch.model.Role;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDAO extends CrudDAOImpl<Role> {

    private SessionFactory sessionFactory;

    @Autowired
    public RoleDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }
}
