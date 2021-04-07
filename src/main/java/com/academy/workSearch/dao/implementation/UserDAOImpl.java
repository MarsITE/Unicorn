package com.academy.workSearch.dao.implementation;

import com.academy.workSearch.dao.UserDAO;
import com.academy.workSearch.model.User;
import com.academy.workSearch.model.enums.AccountStatus;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
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
      User user = session.createQuery("from User where email = :email", User.class)
               .setParameter("email", email)
               .uniqueResult();
      return Optional.ofNullable(user);

    }

    @Override
    public User deleteByEmail(String email) {// not Optional
        User user = getByEmail(email).orElseThrow();
        user.setAccountStatus(AccountStatus.DELETED);
        return user;
    }
}
