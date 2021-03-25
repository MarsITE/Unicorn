package com.academy.workSearch.service;

import com.academy.workSearch.dto.UserInfoDTO;

public interface UserInfoService {
    void save(UserInfoDTO userInfo);

    void update(UserInfoDTO userInfo);

    void updateImage(String imageUrl, String id);
}
