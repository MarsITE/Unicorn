package com.academy.workSearch.service;

import com.academy.workSearch.dto.UserInfoDTO;
import org.springframework.web.multipart.MultipartFile;

public interface UserInfoService {
    void save(UserInfoDTO userInfo);

    boolean updateImage(MultipartFile photo, String id);

}
