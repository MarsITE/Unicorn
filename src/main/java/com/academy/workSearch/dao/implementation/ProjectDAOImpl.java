package com.academy.workSearch.dao.implementation;

import com.academy.workSearch.dao.ProjectDAO;
import com.academy.workSearch.model.Project;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class ProjectDAOImpl extends CrudDAOImpl<Project> implements ProjectDAO {

    @Autowired
    public ProjectDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Page<Project> findAllByPageWithSortOrder(Pageable pageable, String sort) {
        Session session = sessionFactory.getCurrentSession();
        String sortOrder = sort.equals("asc") ? "asc" : "desc";
        Query<Project> query = session.createQuery("from Project order by creation_date " + sortOrder, Project.class);
        query.setFirstResult((pageable.getPageNumber()-1) * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        return new PageImpl<>(query.getResultList());
    }

    @Override
    public Page<Project> findAllByOwnerId(Pageable pageable, String sort, String ownerId) {
        Session session = sessionFactory.getCurrentSession();
        String sortOrder = sort.equals("asc") ? "asc" : "desc";
        Query<Project> query = session.createQuery("from Project where :owner_id = owner_id order by creation_date " + sortOrder, Project.class);
        query.setParameter("owner_id", UUID.fromString(ownerId));
        query.setFirstResult((pageable.getPageNumber()-1) * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        return new PageImpl<>(query.getResultList());
    }

    @Override
    public Page<Project> findAllByWorkerId(Pageable pageable, String sort, String workerId) {
        Session session = sessionFactory.getCurrentSession();
        String sortOrder = sort.equals("asc") ? "asc" : "desc";
        Query<Project> query = session.createQuery("from Project where :worker_id = worker_id order by creation_date " + sortOrder, Project.class);
        query.setParameter("worker_id", UUID.fromString(workerId));
        query.setFirstResult((pageable.getPageNumber()-1) * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        return new PageImpl<>(query.getResultList());
    }

    @Override
    public Page<Project> searchBySkill(List<String> skills, Pageable pageable, String sort) {
        Session session = sessionFactory.getCurrentSession();
        Query<Project> query;
        query = session.createQuery("select distinct p from Project p join p.skills sk where sk.name in :skills order by p.creationDate desc", Project.class)
                .setParameter("skills", skills);
        query.setFirstResult((pageable.getPageNumber()-1) * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        return new PageImpl<>(query.getResultList());
    }

    @Override
    public Long getAllProjectsCount() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("select count(*) from Project");
        return (Long)query.uniqueResult();
    }

    @Override
    public Long getAllProjectsCountBySkills(List<String> skills) {
        Session session = sessionFactory.getCurrentSession();
        final String userSkillsString = skills.stream()
                .map(String::valueOf)
                .collect(Collectors.joining("', '"));
        String sqlQuery = String.format("select count(distinct p.project_id) from projects p join projects_skills ps on p.project_id = ps.project_id join skills s on ps.skill_id = s.skill_id where s.name in ('%s')",userSkillsString);
        Query query = session.createNativeQuery(sqlQuery);
        return ((Number)query.getSingleResult()).longValue();
    }

    @Override
    public boolean isPresentProjectByNameByUserId(String name, String id) {
        Session session = sessionFactory.getCurrentSession();
        String sqlQuery = String.format("SELECT COUNT (name) FROM projects WHERE name = '%s' AND owner_id = '%s'", name, id);
        Query query = session.createNativeQuery(sqlQuery);
        return ((Number) query.getSingleResult()).intValue() != 0;
    }
}
