package com.academy.workSearch.service.implementation;

import com.academy.workSearch.dao.implementation.SkillDAOImpl;
import com.academy.workSearch.dto.SkillDTO;
import com.academy.workSearch.dto.SkillDetailsDTO;
import com.academy.workSearch.exceptionHandling.exceptions.NoSuchEntityException;
import com.academy.workSearch.exceptionHandling.exceptions.NotUniqueEntityException;
import com.academy.workSearch.model.Mail;
import com.academy.workSearch.model.Skill;
import com.academy.workSearch.service.EmailService;
import com.academy.workSearch.service.SkillService;
import com.academy.workSearch.service.UserInfoService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.academy.workSearch.dto.mapper.SkillDetailsMapper.SKILL_DETAILS_MAPPER;
import static com.academy.workSearch.dto.mapper.SkillMapper.SKILL_MAPPER;
import static com.academy.workSearch.exceptionHandling.MessageConstants.*;

@Service
@AllArgsConstructor
public class SkillServiceImpl implements SkillService {
    private final SkillDAOImpl skillDAO;
    private final Logger logger = LoggerFactory.getLogger(SkillServiceImpl.class);
    private final FreeMarkerConfigurer freemarkerConfigurer;
    private final EmailService emailService;
    private final UserInfoService userInfoService;

    /**
     * post construct set class type for dao
     */
    @PostConstruct
    private void setTypeClass() {
        skillDAO.setClazz(Skill.class);
    }


    /**
     *
     * @return list of all skills
     */
    @Transactional(readOnly = true)
    public List<SkillDetailsDTO> findAll() {
        return SKILL_DETAILS_MAPPER.toSkillsDto(skillDAO.findAll());
    }

    /**
     * @param enabled  status of skill (true or false)
     * @return list of all skills which corresponds status enabled
     */
    @Transactional(readOnly = true)
    public List<SkillDTO> findAllEnabled(Boolean enabled) {
        return SKILL_MAPPER.toSkillsDto(skillDAO.getAllEnabled(enabled));
    }

    /**
     * @param userId  id of user
     * @return list of all skills for user with corresponding id
     */
    @Transactional(readOnly = true)
    public List<SkillDetailsDTO> findAllByUserId(UUID userId) {
        return SKILL_DETAILS_MAPPER.toSkillsDto(skillDAO.getAllByUserId(userId));
    }

    /**
     * @param skillName  name of skill you need to check in DB
     * @return true or false
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isPresentSkillByName(String skillName) {
        return skillDAO.getByName(skillName).isPresent();
    }

    /**
     *
     * @param skills list of new skills
     * @param userInfoId userInfoId of worker(user)
     * @return  saved skills
     */
    @Override
    @Transactional
    public List<SkillDetailsDTO> saveWorkerSkills(List<SkillDetailsDTO> skills, UUID userInfoId) {
        return userInfoService.addSkills(userInfoId, skills);
    }

    @Override
    @Transactional
    public SkillDetailsDTO deleteByUserInfoIdBySkillId(UUID userInfoId, UUID skillId) {
        Skill skill = skillDAO.get(skillId)
                .orElseThrow(() -> new NoSuchEntityException(NO_SUCH_SKILL + skillId));
        return userInfoService.deleteSkill(userInfoId, SKILL_DETAILS_MAPPER.toDto(skill));
    }

    @Override
    public void sendEmail(String email, List<SkillDetailsDTO> skills) {
        Function<Stream<SkillDetailsDTO>, List<String>> getSkillNames = skillsStream ->
                skillsStream.map(SkillDetailsDTO::getName).collect(Collectors.toList());
        List<String> approvedSkillNames = getSkillNames.apply(skills.stream().filter(SkillDetailsDTO::isEnabled));
        List<String> unapprovedSkillNames = getSkillNames.apply(skills.stream().filter(skill -> !skill.isEnabled()));
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("email", email);
            model.put("isPresentApprovedSkills", !approvedSkillNames.isEmpty());
            model.put("approvedSkills", approvedSkillNames);
            model.put("isPresentUnapprovedSkills",!unapprovedSkillNames.isEmpty());
            model.put("unapprovedSkills", unapprovedSkillNames);
            Template template = freemarkerConfigurer.getConfiguration().getTemplate("add-new-skills-message.ftl");
            String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            Mail mail = new Mail();
            mail.setSubject("Your new skills at Worksearch.com");
            mail.setEmail(email);
            mail.setMessage(content);
            emailService.sendHtmlMessage(mail);
        } catch (IOException | TemplateException e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * @param skill  skill you need to save in DB
     * @return skillDAO of saved skill
     */
    @Transactional
    public SkillDetailsDTO save(SkillDetailsDTO skill) {
        if (isPresentSkillByName(skill.getName())) {
            throw new NotUniqueEntityException(SKILL_EXISTS);
        }
        return SKILL_DETAILS_MAPPER.toDto(skillDAO.save(SKILL_DETAILS_MAPPER.toEntity(skill)));
    }

    /**
     * @param skills  list of skill you need to save in DB
     * @return list of saved skill
     */
    @Override
    @Transactional
    public List<SkillDetailsDTO> saveSkillList(List<SkillDetailsDTO> skills) {
        return skills.stream()
                .map(skill -> skill.getName().trim())
                .map(skillName -> {
                    Optional<Skill> skillInDB = skillDAO.getByName(skillName);
                    if(skillInDB.isEmpty()){
                        Skill newSkill = new Skill();
                        newSkill.setName(skillName);
                        return skillDAO.save(newSkill);
                    }
                    return skillInDB.get();
                })
                .map(SKILL_DETAILS_MAPPER::toDto)
                .collect(Collectors.toList());
    }

    /**
     * @param skill skill you need to update in DB
     * @return skillDAO of updated skill
     */
    @Override
    @Transactional
    public SkillDetailsDTO update(SkillDetailsDTO skill) {
        UUID id = skill.getSkillId();
        Skill skillInDB = skillDAO.get(id)
                .orElseThrow(() -> new NoSuchEntityException(NO_SUCH_SKILL + id));
        skillInDB.setName(skill.getName());
        skillInDB.setEnabled(skill.isEnabled());
        return SKILL_DETAILS_MAPPER.toDto(skillDAO.save(skillInDB));
    }

    /**
     * @param id
     * @return skill
     */
    @Override
    @Transactional(readOnly = true)
    public SkillDetailsDTO get(UUID id) {
        Skill skill = skillDAO.get(id)
                .orElseThrow(() -> new NoSuchEntityException(NO_SUCH_SKILL + id));
        return SKILL_DETAILS_MAPPER.toDto(skill);
    }

    /**
     * @param name name of skill you need to recive from DB
     * @return skill with corresponding name
     */
    @Override
    @Transactional(readOnly = true)
    public SkillDetailsDTO getByName(String name) {
        Skill skillByName = skillDAO.getByName(name)
                .orElseThrow(() -> new NoSuchEntityException(NO_SUCH_SKILL + name));
        return SKILL_DETAILS_MAPPER.toDto(skillByName);
    }

    /**
     *
     * @param id id of skill need to delete
     * @return  SkillDetailsDTO of deleted skill
     */
    @Override
    @Transactional
    public SkillDetailsDTO delete(UUID id) {
        Skill skill = skillDAO.get(id)
                .orElseThrow(() -> new NoSuchEntityException(NO_SUCH_SKILL + id));
        skillDAO.delete(id);
        return SKILL_DETAILS_MAPPER.toDto(skill);
    }
}
