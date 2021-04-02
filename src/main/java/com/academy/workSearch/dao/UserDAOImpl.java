package com.academy.workSearch.dao;

import com.academy.workSearch.model.AccountStatus;
import com.academy.workSearch.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAOImpl extends CrudDAOImpl<User> implements UserDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public User getByEmail(String email) {
        Session session = sessionFactory.getCurrentSession();
        return (User) session.createQuery("select u from User u where u.email = : email")
                .setParameter("email", email).uniqueResult();
    }

    @Override
    public void deleteByEmail(String email) {
        User user = getByEmail(email);
        user.setAccountStatus(AccountStatus.DELETED);
        save(user);
    }


}
