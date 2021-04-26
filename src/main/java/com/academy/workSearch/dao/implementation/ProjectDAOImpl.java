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
import java.util.UUID;

@Repository
public class ProjectDAOImpl extends CrudDAOImpl<Project> implements ProjectDAO {

    @Autowired
    public ProjectDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<Project> findAllByPageWithSortOrder(int page, int maxResult, int maxNavigationPage, String sort) {
        Session session = sessionFactory.getCurrentSession();
        String sortOrder = sort.equals("asc") ? "asc" : "desc";
        Query<Project> query = session.createQuery("from Project order by creation_date " + sortOrder, Project.class);

        PaginationResult<Project> paginationResult = new PaginationResult<>(query, page, maxResult, maxNavigationPage);

        return paginationResult.getList();
    }

    @Override
    public List<Project> findAllByOwnerId(int page, int maxResult, int maxNavigationPage, String sort, String ownerId) {
        Session session = sessionFactory.getCurrentSession();
        String sortOrder = sort.equals("asc") ? "asc" : "desc";
        Query<Project> query = session.createQuery("from Project where :owner_id = owner_id order by creation_date " + sortOrder, Project.class);
        query.setParameter("owner_id", UUID.fromString(ownerId));
        PaginationResult<Project> paginationResult = new PaginationResult<>(query, page, maxResult, maxNavigationPage);

        return paginationResult.getList();
    }

    @Override
    public List<Project> findAllByWorkerId(int page, int maxResult, int maxNavigationPage, String sort, String workerId) {
        Session session = sessionFactory.getCurrentSession();
        String sortOrder = sort.equals("asc") ? "asc" : "desc";
        Query<Project> query = session.createQuery("from Project where :worker_id = worker_id order by creation_date " + sortOrder, Project.class);
        query.setParameter("worker_id", UUID.fromString(workerId));
        PaginationResult<Project> paginationResult = new PaginationResult<>(query, page, maxResult, maxNavigationPage);

        return paginationResult.getList();
    }

    @Override
    public boolean isPresentProjectByNameByUserId(String name, String id) {
        Session session = sessionFactory.getCurrentSession();
        String sqlQuery = String.format("SELECT COUNT (name) FROM projects WHERE name = '%s' AND owner_id = '%s'", name, id);
        Query query = session.createNativeQuery(sqlQuery);
        return ((Number) query.getSingleResult()).intValue() != 0;
    }
}
