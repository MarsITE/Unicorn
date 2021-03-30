package com.academy.workSearch.dao;

import com.academy.workSearch.model.Role;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDAOImpl extends CrudDAOImpl<Role> implements RoleDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Role getByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Role where name = :name");
        query.setParameter("name", name);
        return (Role) query.list().get(0);
    }
}
