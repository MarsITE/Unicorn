package com.academy.workSearch.service;

import com.academy.workSearch.dao.CrudDAO;
import com.academy.workSearch.dto.UserInfoDTO;
import com.academy.workSearch.model.UserInfo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.academy.workSearch.dto.mapper.UserInfoMapper.USER_INFO_MAPPER;

@Service
@Transactional
@AllArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private final CrudDAO<UserInfo> userInfoDAO;

    public void save(UserInfoDTO userInfo) {
        userInfoDAO.save(USER_INFO_MAPPER.toUserInfo(userInfo));
    }

    public void update(UserInfoDTO userInfo) {
        userInfoDAO.save(USER_INFO_MAPPER.toUserInfo(userInfo));
    }

    @Override
    public void updateImage(String imageUrl, String id) {
        UUID uuid = UUID.fromString(id);
        UserInfo userInfo = userInfoDAO.get(uuid);
        userInfo.setImageUrl(imageUrl);
        userInfoDAO.save(userInfo);
    }
}
