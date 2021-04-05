package com.academy.workSearch.dao.implementation;

import com.academy.workSearch.dao.UserDAO;
import com.academy.workSearch.model.enums.AccountStatus;
import com.academy.workSearch.model.User;
import com.academy.workSearch.model.enums.AccountStatus;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDAOImpl extends CrudDAOImpl<User> implements UserDAO {

    @Autowired
    public UserDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<User> getByEmail(String email) {
        Session session = sessionFactory.getCurrentSession();
        Query<User> query = session.createQuery("from User where email = :email", User.class);
        query.setParameter("email", email);
        if (query.list().size() == 0) {
            return Optional.of(query.list().get(0));
        } else {
            return Optional.ofNullable(query.list().get(0));
        }
    }

        @Override
        public Optional<User> deleteByEmail(String email){// not Optional
            Optional<User> user = getByEmail(email);
            user.stream().map(user1 -> AccountStatus.DELETED);
            return user;
        }
    }
