package com.academy.workSearch.dao.implementation;

import com.academy.workSearch.dao.UserDAO;
import com.academy.workSearch.model.User;
import com.academy.workSearch.model.enums.AccountStatus;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDAOImpl extends CrudDAOImpl<User> implements UserDAO {

    @Autowired
    public UserDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<User> getByEmail(String email) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.createQuery("from User where email = :email", User.class)
                .setParameter("email", email)
                .uniqueResult();
        return Optional.ofNullable(user);
    }

    @Override
    public User deleteByEmail(String email) {
        User user = getByEmail(email).orElseThrow();
        user.setAccountStatus(AccountStatus.DELETED);
        return user;
    }

    @Override
    public Optional<User> getByToken(String registrationToken) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.createQuery("from User where registrationToken = :registrationToken", User.class)
                .setParameter("registrationToken", registrationToken)
                .uniqueResult();
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findNotActive() {
        Session session = sessionFactory.getCurrentSession();
        Query<User> query = session.createQuery("from User where account_status='NOT_ACTIVE'", User.class);
        return query.getResultList();
    }

}
