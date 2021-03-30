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

    private final SessionFactory sessionFactory;

    @Autowired
    public ProjectDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Project> findLast(int page, int maxResult) {
        Session session = sessionFactory.getCurrentSession();

        Query<Project> query = session.createQuery("from Project order by creation_date desc", Project.class);
        PaginationResult<Project> paginationResult = new PaginationResult(query, page, maxResult, 10);

        return paginationResult.getList();
    }
}
