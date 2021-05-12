package com.academy.workSearch.service;

import com.academy.workSearch.dto.ProjectDTO;
import com.academy.workSearch.dto.SkillDTO;
import com.academy.workSearch.dto.SkillDetailsDTO;
import com.academy.workSearch.dto.UserInfoDTO;
import com.academy.workSearch.model.User;
import com.academy.workSearch.model.UserInfo;

import java.util.List;
import java.util.UUID;

public interface SkillService {
    List<SkillDetailsDTO> findAll();

    List<SkillDTO> findAllByEnabled(Boolean enabled);

    List<SkillDetailsDTO> findAllByUserId(UUID userId);

    SkillDetailsDTO save(SkillDetailsDTO skill);

    List<SkillDetailsDTO> saveSkillList(List<SkillDetailsDTO> skills);

    SkillDetailsDTO update(SkillDetailsDTO skill);

    SkillDetailsDTO delete(UUID id);

    SkillDetailsDTO get(UUID id);

    SkillDetailsDTO getByName(String name);

    boolean isPresentSkillByName(String skillName);

    List<SkillDetailsDTO> saveWorkerSkills(List<SkillDetailsDTO> skills, UUID userInfoId);

    SkillDetailsDTO deleteByUserInfoIdBySkillId(UUID userInfoId, UUID skillId);

    void sendEmail(String email, List<SkillDetailsDTO> skills);
}
