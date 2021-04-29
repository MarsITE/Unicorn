package com.academy.workSearch.dao.implementation;

import com.academy.workSearch.dao.ProjectUserInfoDAO;
import com.academy.workSearch.model.ProjectUserInfo;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectUserInfoDAOImpl extends CrudDAOImpl<ProjectUserInfo> implements ProjectUserInfoDAO {

    @Autowired
    public ProjectUserInfoDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

}
