package com.academy.workSearch.service;

import com.academy.workSearch.dto.PhotoDTO;
import com.academy.workSearch.dto.UserInfoDTO;
import org.springframework.web.multipart.MultipartFile;

public interface UserInfoService {

    UserInfoDTO save(UserInfoDTO userInfo);

    boolean updateImage(MultipartFile photo, String id);

    PhotoDTO loadPhoto(String imageName);
}
