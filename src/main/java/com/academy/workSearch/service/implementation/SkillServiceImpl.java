package com.academy.workSearch.service.implementation;

import com.academy.workSearch.dao.implementation.SkillDAOImpl;
import com.academy.workSearch.dto.SkillDTO;
import com.academy.workSearch.dto.SkillDetailsDTO;
import com.academy.workSearch.exceptionHandling.exceptions.NoSuchEntityException;
import com.academy.workSearch.model.Skill;
import com.academy.workSearch.service.SkillService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.UUID;

import static com.academy.workSearch.dto.mapper.SkillDetailsMapper.SKILL_DETAILS_MAPPER;
import static com.academy.workSearch.dto.mapper.SkillMapper.SKILL_MAPPER;
import static com.academy.workSearch.exceptionHandling.MessageConstants.NO_SUCH_SKILL;

@Service
@Transactional
@AllArgsConstructor
public class SkillServiceImpl implements SkillService {
    private final SkillDAOImpl skillDAO;
    private final String NO_SUCH_SKILL_FORMAT = "%s = %s";

    @PostConstruct
    private void setTypeClass() {
        skillDAO.setClazz(Skill.class);
    }

    public List<SkillDetailsDTO> findAll() {
        return SKILL_DETAILS_MAPPER.toSkillsDto(skillDAO.findAll());
    }

    public List<SkillDTO> findAllEnabled(Boolean enabled) {
        return SKILL_MAPPER.toSkillsDto(skillDAO.getAllEnabled(enabled));
    }

    public SkillDetailsDTO save(SkillDetailsDTO skill) {
        return SKILL_DETAILS_MAPPER.toDto(skillDAO.save(SKILL_DETAILS_MAPPER.toEntity(skill)));
    }

    public SkillDetailsDTO update(SkillDetailsDTO skill) {
        UUID id = skill.getSkillId();
        Skill skillInDB = skillDAO.get(id)
                .orElseThrow(() ->
                        new NoSuchEntityException(String.format(NO_SUCH_SKILL_FORMAT, NO_SUCH_SKILL + "id", id)));
        skillInDB.setName(skill.getName());
        skillInDB.setEnabled(skill.isEnabled());
        return SKILL_DETAILS_MAPPER.toDto(skillDAO.save(skillInDB));
    }

    public SkillDetailsDTO get(UUID id) {
        Skill skill = skillDAO.get(id)
                .orElseThrow(() -> new NoSuchEntityException(NO_SUCH_SKILL + id));
        return SKILL_DETAILS_MAPPER.toDto(skill);
    }

    public SkillDetailsDTO getByName(String name) {
        Skill skillByName = skillDAO.getByName(name)
                .orElseThrow(() -> new NoSuchEntityException(NO_SUCH_SKILL + name));
        return SKILL_DETAILS_MAPPER.toDto(skillByName);
    }

}
