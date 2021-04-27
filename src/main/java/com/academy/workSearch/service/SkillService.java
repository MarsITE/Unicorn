package com.academy.workSearch.service;

import com.academy.workSearch.dto.ProjectDTO;
import com.academy.workSearch.dto.SkillDTO;
import com.academy.workSearch.dto.SkillDetailsDTO;
import java.util.List;
import java.util.UUID;

public interface SkillService {
    List<SkillDetailsDTO> findAll();

    List<SkillDTO> findAllEnabled(Boolean enabled);

    List<SkillDTO> findAllByUserId(UUID userId);

    SkillDetailsDTO save(SkillDetailsDTO skill);

    SkillDetailsDTO update(SkillDetailsDTO skill);

    SkillDetailsDTO delete(UUID id);

    SkillDetailsDTO get(UUID id);

    SkillDetailsDTO getByName(String name);

    boolean isPresentSkillByName(String skillName);
}
