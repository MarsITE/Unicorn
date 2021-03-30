package com.academy.workSearch.dao.implementation;

import com.academy.workSearch.dao.UserInfoDAO;
import com.academy.workSearch.model.UserInfo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class UserInfoDAOImpl extends CrudDAOImpl<UserInfo> implements UserInfoDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public UserInfoDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public UUID saveAndGetId(UserInfo userInfo) {
        Session session = sessionFactory.getCurrentSession();
        return (UUID) session.save(userInfo);
    }
}
