package com.academy.workSearch.dao.implementation;

import com.academy.workSearch.dao.UserDAO;
import com.academy.workSearch.model.User;
import com.academy.workSearch.model.enums.AccountStatus;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAOImpl extends CrudDAOImpl<User> implements UserDAO {

    @Autowired
    public UserDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public User getByEmail(String email) {
        Session session = sessionFactory.getCurrentSession();
        return (User) session.createQuery("select u from User u where u.email =: email")
                .setParameter("email", email).uniqueResult();
    }

    @Override
    public void deleteByEmail(String email) {
        User user = getByEmail(email);
        user.setAccountStatus(AccountStatus.DELETED);
        save(user);
    }
}
