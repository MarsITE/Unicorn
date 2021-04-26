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
    public List<Project> findAllByPageWithSortOrder(int page, int maxResult, int maxNavigationPage, String sort) {
        Session session = sessionFactory.getCurrentSession();
        Query<Project> query;

        String sortOrder = sort.equals("asc") ? "asc" : "desc";
        query = session.createQuery("from Project order by creation_date " + sortOrder, Project.class);

        PaginationResult<Project> paginationResult = new PaginationResult<>(query, page, maxResult, maxNavigationPage);

        return paginationResult.getList();
    }

    @Override
    public List<Project> searchBySkill(List<String> skills, int page, int maxResult, int maxNavigationPage, String sort) {
        Session session = sessionFactory.getCurrentSession();
        Query<Project> query;
        query = session.createQuery("select p from Project p  join p.skills sk where sk.name in :skills", Project.class).setParameter("skills", skills);
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
