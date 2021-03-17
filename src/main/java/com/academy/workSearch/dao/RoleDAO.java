//package com.academy.workSearch.dao;
//
//import com.academy.workSearch.model.Role;
//import com.academy.workSearch.model.User;
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
//public class RoleDAO implements CrudDAO<Role> {
//
//    @Autowired
//    private SessionFactory sessionFactory;
//
//    @Override
//    public List<User> findAll() {
//        Session session = sessionFactory.getCurrentSession();
//        Query<Role> query = session.createQuery("from Role", Role.class);
//        return query.getResultList();
//    }
//
//    @Override
//    public void save(Role role) {
//        Session session = sessionFactory.getCurrentSession();
//        session.saveOrUpdate(role);
//    }
//
//    @Override
//    public User get(UUID id) {
//        Session session = sessionFactory.getCurrentSession();
//        return session.get(Role.class, id);
//    }
//
//    @Override
//    public void delete(UUID id) {
//        Session session = sessionFactory.getCurrentSession();
//        Query<Role> query = session.createQuery("delete from Role where id =:roleId");
//        query.setParameter("roleId", id);
//        query.executeUpdate();
//    }
//}
