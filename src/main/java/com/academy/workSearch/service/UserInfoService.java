package com.academy.workSearch.service;

import com.academy.workSearch.dao.CrudDAO;
import com.academy.workSearch.model.UserInfo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class UserInfoService {

    @Autowired
    private final CrudDAO<UserInfo> userInfoDAO;

    public List<UserInfo> findAll() {
        return userInfoDAO.findAll();
    }

    public void save(UserInfo userInfo) {
        userInfoDAO.save(userInfo);
    }

    public UserInfo get(UUID id) {
        return userInfoDAO.get(id);
    }

    public void delete(UUID id) {
        userInfoDAO.delete(id);
    }
}
