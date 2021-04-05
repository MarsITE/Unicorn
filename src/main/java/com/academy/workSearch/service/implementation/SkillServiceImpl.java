package com.academy.workSearch.service.implementation;

import com.academy.workSearch.dao.*;
import com.academy.workSearch.dao.implementation.SkillDAOImpl;
import com.academy.workSearch.dto.SkillDTO;
import com.academy.workSearch.dto.SkillDetailsDTO;
import com.academy.workSearch.exceptionHandling.exceptions.NoSuchEntityException;
import com.academy.workSearch.model.Skill;
import com.academy.workSearch.service.SkillService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.academy.workSearch.dto.mapper.SkillDetailsMapper.SKILL_DETAILS_MAPPER;
import static com.academy.workSearch.dto.mapper.SkillMapper.SKILL_MAPPER;

@Service
@Transactional
@AllArgsConstructor
public class SkillServiceImpl  implements SkillService  {
    private final CrudDAO<Skill> skillCrudDAO;
    private final SkillDAOImpl skillDAO;

    public List<SkillDetailsDTO> findAll() {
        return SKILL_DETAILS_MAPPER.toSkillsDto(skillCrudDAO.findAll());
    }

    public List<SkillDTO> findAllEnabled(Boolean enabled){
        return SKILL_MAPPER.toSkillsDto(skillDAO.getAllEnabled(enabled));
    }

    public SkillDTO save(SkillDTO skill) {
        return SKILL_MAPPER.toDto(skillDAO.save(SKILL_MAPPER.toEntity(skill)));
    }

    public SkillDetailsDTO update(SkillDetailsDTO skill) {
        Skill skill1 = skillDAO.getByName(skill.getName()).get();
        Skill skill2 = SKILL_DETAILS_MAPPER.toEntity(skill);
        skill2.setSkillId(skill1.getSkillId());
        skill2.setName(skill.getName());
        skill2.setEnabled(skill.isEnabled());
        skillCrudDAO.save(skill2);
        return SKILL_DETAILS_MAPPER.toDto(skill2);
    }

    public Optional<SkillDTO> get(UUID id) {
      Skill skill = skillDAO.get(id)
               .orElseThrow(()-> new NoSuchEntityException("There is no skill with id = {}" + id));
       SkillDTO skillDTO1 = SKILL_MAPPER.toDto(skill);
        return Optional.of(skillDTO1);
    }

    public Optional<SkillDTO> getByName(String name) {
        Optional<Skill> skillDTO = skillDAO.getByName(name);
        Skill skill = skillDTO.get();
        SkillDTO skillDTO1 = SKILL_MAPPER.toDto(skill);
        return Optional.of(skillDTO1);
    }

}
