package com.academy.workSearch.dao.implementation;

import com.academy.workSearch.dao.RoleDAO;
import com.academy.workSearch.model.Role;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class RoleDAOImpl extends CrudDAOImpl<Role> implements RoleDAO {

    @Autowired
    public RoleDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Role> getByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        Role role = session.createQuery("from Role where name = :name", Role.class)
                .setParameter("name", name).setMaxResults(1)
                .uniqueResult();
        return Optional.ofNullable(role);
    }
}
