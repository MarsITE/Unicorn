package com.academy.workSearch.dao.implementation;

import com.academy.workSearch.dao.ProjectDAO;
import com.academy.workSearch.dao.pagination.PaginationResult;
import com.academy.workSearch.model.Project;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class ProjectDAOImpl extends CrudDAOImpl<Project> implements ProjectDAO {

    @Autowired
    public ProjectDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<Project> findLast(int page, int maxResult, int maxNavigationPage, String sort) {
        Session session = sessionFactory.getCurrentSession();
        Query<Project> query;

        if (sort.equals("asc")) {
            query = session.createQuery("from Project order by creation_date asc", Project.class);
        } else {
            query = session.createQuery("from Project order by creation_date desc", Project.class);
        }


        PaginationResult<Project> paginationResult = new PaginationResult<>(query, page, maxResult, maxNavigationPage);

        return paginationResult.getList();
    }

    @Override
    public List<Project> searchBySkill(List<String> skills, int page, int maxResult, int maxNavigationPage, String sort) {
        Session session = sessionFactory.getCurrentSession();
        Query query;
        query = session.createQuery("select p from Project p  join p.skills sk where sk.name in :skills", Project.class).setParameter("skills", skills);
        PaginationResult<Project> paginationResult = new PaginationResult<>(query, page, maxResult, maxNavigationPage);
        return query.getResultList();
    }
}
