package com.academy.workSearch.service;

import com.academy.workSearch.dao.*;
import com.academy.workSearch.dto.SkillDTO;
import com.academy.workSearch.model.Skill;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.ValidationException;
import java.util.List;
import java.util.UUID;

import static com.academy.workSearch.dto.mapper.SkillMapper.SKILL_MAPPER;

@Service
@Transactional
@AllArgsConstructor
public class SkillServiceImpl implements SkillService{

    private final CrudDAO<Skill> skillCrudDAO;
    private final SkillDAOImpl skillDAO;

    public List<SkillDTO> findAll() {
        return SKILL_MAPPER.toSkillsDto(skillCrudDAO.findAll());
    }

    public void save(SkillDTO skill) throws ValidationException {
        skillDAO.save(SKILL_MAPPER.toEntity(skill));
    }

    @Override
    public SkillDTO update(SkillDTO skill) throws ValidationException {
        Skill skill1 = skillDAO.getByName(skill.getName());
        Skill skill2 = SKILL_MAPPER.toEntity(skill);
        skill2.setSkillId(skill1.getSkillId());
        skill2.setName(skill.getName());
        skill2.setEnabled(skill.isEnabled());
        skillCrudDAO.save(skill2);
        return SKILL_MAPPER.toDto(skill2);
    }

    public SkillDTO get(UUID id) {
        return SKILL_MAPPER.toDto(skillDAO.get(id));
    }

    public SkillDTO getByName(String name) {
        return SKILL_MAPPER.toDto(skillDAO.getByName(name));
    }

    public void delete(UUID id) {
        skillDAO.delete(id);
    }
}
