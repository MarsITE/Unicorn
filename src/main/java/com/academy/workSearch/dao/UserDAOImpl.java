package com.academy.workSearch.dao;

import com.academy.workSearch.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public UserDAOImpl() {
    }

    public List<User> findAll() {
        Session session = sessionFactory.getCurrentSession();
        Query<User> query = session.createQuery("from User", User.class);
        List<User> allUsers = query.getResultList();
        return allUsers;
    }

    @Override
    public void saveUser(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(user);

    }

    @Override
    public User getUser(UUID id) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.get(User.class, id);
        return user;
    }

    @Override
    public void deleteUser(UUID id) {
        Session session = sessionFactory.getCurrentSession();
        Query<User> query = session.createQuery("delete from User where id =:userId");
        query.setParameter("userId", id);
        query.executeUpdate();
    }


}
