package com.academy.workSearch.service.implementation;

import com.academy.workSearch.dto.SkillDTO;
import com.academy.workSearch.dto.UserDTO;
import com.academy.workSearch.model.Skill;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

public interface SkillService {
    List<SkillDTO> findAll();
    void save(SkillDTO skill);
    SkillDTO update(SkillDTO skill);
    SkillDTO get(UUID id);
    SkillDTO getByName(String name);
}
