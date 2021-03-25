package com.academy.workSearch.dao;

import com.academy.workSearch.model.User;
import com.academy.workSearch.model.UserInfo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class UserInfoDAOImpl implements CrudDAO<UserInfo>, UserInfoDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<UserInfo> findAll() {
        Session session = sessionFactory.getCurrentSession();
        Query<UserInfo> query = session.createQuery("from UserInfo", UserInfo.class);
        List<UserInfo> list = query.getResultList();
        return list;
    }

    @Override
    public void save(UserInfo userInfo) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(userInfo);
    }

    @Override
    public UserInfo get(UUID id) {
        Session session = sessionFactory.getCurrentSession();
        return session.load(UserInfo.class, id);
    }

    @Override
    public void delete(UUID id) {
        Session session = sessionFactory.getCurrentSession();
        Query<UserInfo> query = session.createQuery("delete from UserInfo where id =:userInfoId");
        query.setParameter("userInfoId", id);
        query.executeUpdate();
    }
}
