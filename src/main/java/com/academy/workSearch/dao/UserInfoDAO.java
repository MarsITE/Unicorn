package com.academy.workSearch.dao;

import com.academy.workSearch.model.UserInfo;

import java.util.UUID;

public interface UserInfoDAO {
    UUID saveAndGetId(UserInfo userInfo);

}
