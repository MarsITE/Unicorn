package com.academy.workSearch.service;

import com.academy.workSearch.dto.SkillDTO;
import com.academy.workSearch.dto.SkillDetailsDTO;
import java.util.List;
import java.util.UUID;

public interface SkillService {
    List<SkillDetailsDTO> findAll();
    List<SkillDTO> findAllEnabled(Boolean enabled);
    void save(SkillDTO skill);
    SkillDetailsDTO update(SkillDetailsDTO skill);
    SkillDetailsDTO get(UUID id);
    SkillDetailsDTO getByName(String name);

}
