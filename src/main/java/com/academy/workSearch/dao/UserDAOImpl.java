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
public class UserDAOImpl implements CrudDAO<User>, UserDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public List<User> findAll() {
        Session session = sessionFactory.getCurrentSession();
        Query<User> query = session.createQuery("from User", User.class);
        return query.getResultList();
    }

    @Override
    public void save(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(user);
    }

    @Override
    public User get(UUID id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(User.class, id);
    }

    @Override
    public void delete(UUID id) {
        Session session = sessionFactory.getCurrentSession();
        Query<User> query = session.createQuery("delete from User where id =:userId");
        query.setParameter("userId", id);
        query.executeUpdate();
    }

    public User getByEmail(String email) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from User where email = :email");
        query.setParameter("email", email);
        return (User) query.list().get(0);
    }

    public UUID getIdByEmail(String email) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(UUID.class, email);
    }
}
