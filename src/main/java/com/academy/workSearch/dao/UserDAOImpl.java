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
        Session session = sessionFactory.openSession();
        Query<User> query = session.createQuery("from User", User.class);
        return query.getResultList();
    }

    @Override
    public void save(User user) {
        Session session = sessionFactory.openSession();
        session.saveOrUpdate(user);
        session.close();
    }

    @Override
    public User get(UUID id) {
        Session session = sessionFactory.openSession();
        return session.get(User.class, id);
    }

    @Override
    public void delete(UUID id) {
        Session session = sessionFactory.openSession();
        Query<User> query = session.createQuery("delete from User where id =:userId");
        query.setParameter("userId", id);
        query.executeUpdate();
        session.close();
    }

    public User getByEmail(String email) {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("from User where email = :email");
        query.setParameter("email", email);
        User e = (User) query.list().get(0);
        session.close();
        return e;
    }

    public UUID getIdByEmail(String email) {
        Session session = sessionFactory.openSession();
        UUID e = session.get(UUID.class, email);
        session.close();
        return e;
    }
}
