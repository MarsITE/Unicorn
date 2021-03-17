//package com.academy.workSearch.dao;
//
//import com.academy.workSearch.model.User;
//import com.academy.workSearch.model.UserInfo;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.query.Query;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.UUID;
//
//@Repository
//public class UserInfoDAO implements CrudDAO<UserInfo> {
//
//    @Autowired
//    private SessionFactory sessionFactory;
//
////    @Override
////    public List<User> findAll() {
////        Session session = sessionFactory.getCurrentSession();
////        Query<UserInfo> query = session.createQuery("from UserInfo", UserInfo.class);
////        return query.getResultList();
////    }
//
//    @Override
//    public void save(UserInfo userInfo) {
//        Session session = sessionFactory.getCurrentSession();
//        session.saveOrUpdate(userInfo);
//    }
//
////    @Override
////    public User get(UUID id) {
////        Session session = sessionFactory.getCurrentSession();
////        return session.get(UserInfo.class, id);
////    }
//
//    @Override
//    public void delete(UUID id) {
//        Session session = sessionFactory.getCurrentSession();
//        Query<UserInfo> query = session.createQuery("delete from UserInfo where id =:userInfoId");
//        query.setParameter("userInfoId", id);
//        query.executeUpdate();
//    }
//}
