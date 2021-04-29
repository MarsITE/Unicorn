package com.academy.workSearch.service.implementation;

import com.academy.workSearch.dao.ProjectUserInfoDAO;
import com.academy.workSearch.model.ProjectUserInfo;
import com.academy.workSearch.service.ProjectUserInfoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@AllArgsConstructor
@Service
public class ProjectUserInfoServiceImpl implements ProjectUserInfoService {

    private final ProjectUserInfoDAO projectUserInfoDAO;

    @PostConstruct
    private void setTypeClass() {
        projectUserInfoDAO.setClazz(ProjectUserInfo.class);
    }
}
