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
public class UserInfoService implements CrudService<UserInfo> {

    @Autowired
    private final CrudDAO<UserInfo> userInfoDAO;

    @Override
    public List<UserInfo> findAll() {
        return userInfoDAO.findAll();
    }

    @Override
    public void save(UserInfo userInfo) {
        userInfoDAO.save(userInfo);
    }

    @Override
    public UserInfo get(UUID id) {
        return userInfoDAO.get(id);
    }

    @Override
    public void delete(UUID id) {
        userInfoDAO.delete(id);
    }
}
