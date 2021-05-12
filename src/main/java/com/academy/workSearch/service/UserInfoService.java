package com.academy.workSearch.service;

import com.academy.workSearch.dto.PhotoDTO;
import com.academy.workSearch.dto.SkillDetailsDTO;
import com.academy.workSearch.dto.UserInfoDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserInfoService {

    UserInfoDTO update(UserInfoDTO userInfo);

    boolean updateImage(MultipartFile photo, String id, long maxFileLength);

    PhotoDTO loadPhoto(String imageName);

    List<SkillDetailsDTO> addSkills(UUID userInfoId, List<SkillDetailsDTO> skills);

    SkillDetailsDTO deleteSkill(UUID userInfoId, SkillDetailsDTO skill);
}
