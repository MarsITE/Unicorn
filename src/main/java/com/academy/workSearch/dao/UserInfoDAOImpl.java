package com.academy.workSearch.dao;

import com.academy.workSearch.model.UserInfo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class UserInfoDAOImpl extends CrudDAOImpl<UserInfo> implements UserInfoDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public UUID saveAndGetId(UserInfo userInfo) {
        Session session = sessionFactory.getCurrentSession();
        return (UUID) session.save(userInfo);
    }
}
