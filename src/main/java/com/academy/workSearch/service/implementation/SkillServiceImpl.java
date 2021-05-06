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
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.jsonwebtoken.lang.Objects;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.academy.workSearch.dto.mapper.SkillDetailsMapper.SKILL_DETAILS_MAPPER;
import static com.academy.workSearch.dto.mapper.SkillMapper.SKILL_MAPPER;
import static com.academy.workSearch.exceptionHandling.MessageConstants.*;

@Service
@Transactional
@AllArgsConstructor
public class SkillServiceImpl implements SkillService {
    private final SkillDAOImpl skillDAO;
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final FreeMarkerConfigurer freemarkerConfigurer;
    private final EmailService emailService;
    private final String NO_SUCH_SKILL_FORMAT = "%s = %s";

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
    public List<SkillDetailsDTO> findAll() {
        return SKILL_DETAILS_MAPPER.toSkillsDto(skillDAO.findAll());
    }

    /**
     * @param enabled  status of skill (true or false)
     * @return list of all skills which corresponds status enabled
     */
    public List<SkillDTO> findAllEnabled(Boolean enabled) {
        return SKILL_MAPPER.toSkillsDto(skillDAO.getAllEnabled(enabled));
    }

    /**
     * @param userId  id of user
     * @return list of all skills for user with corresponding id
     */
    public List<SkillDTO> findAllByUserId(UUID userId) {
        return SKILL_MAPPER.toSkillsDto(skillDAO.getAllByUserId(userId));
    }

    /**
     * @param skillName  name of skill you need to check in DB
     * @return true or false
     */
    @Override
    public boolean isPresentSkillByName(String skillName) {
        return skillDAO.getByName(skillName).isPresent();
    }

    /**
     * @param skill  skill you need to save in DB
     * @return skillDAO of saved skill
     */
    public SkillDetailsDTO save(SkillDetailsDTO skill) {
        if (isPresentSkillByName(skill.getName())) {
            throw new NotUniqueEntityException(SKILL_EXISTS);
        }
        return SKILL_DETAILS_MAPPER.toDto(skillDAO.save(SKILL_DETAILS_MAPPER.toEntity(skill)));
    }

    /**
     * @param skill skill you need to update in DB
     * @return skillDAO of updated skill
     */
    public SkillDetailsDTO update(SkillDetailsDTO skill) {
        UUID id = skill.getSkillId();
        Skill skillInDB = skillDAO.get(id)
                .orElseThrow(() ->
                        new NoSuchEntityException(String.format(NO_SUCH_SKILL_FORMAT, NO_SUCH_SKILL + "id", id)));
        skillInDB.setName(skill.getName());
        skillInDB.setEnabled(skill.isEnabled());
        return SKILL_DETAILS_MAPPER.toDto(skillDAO.save(skillInDB));
    }

    /**
     * @param id
     * @return skill
     */
    public SkillDetailsDTO get(UUID id) {
        Skill skill = skillDAO.get(id)
                .orElseThrow(() -> new NoSuchEntityException(NO_SUCH_SKILL + id));
        return SKILL_DETAILS_MAPPER.toDto(skill);
    }

    /**
     * @param name name of skill you need to recive from DB
     * @return skill with corresponding name
     */
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
    public SkillDetailsDTO delete(UUID id) {
        Skill skill = skillDAO.get(id)
                .orElseThrow(() -> new NoSuchEntityException(NO_SUCH_SKILL + id));
        skillDAO.delete(id);
        return SKILL_DETAILS_MAPPER.toDto(skill);
    }

    /**
     *
     * @param approvedSkills list of approved skills
     * @param unapprovedSkills list of unapproved skills
     * @return  void
     */
    @Override
    public void sendEmail(String email, List<SkillDetailsDTO> approvedSkills, List<SkillDetailsDTO>unapprovedSkills) {
        Function<List<SkillDetailsDTO>, List<String>> getSkillNames = skills ->
                SKILL_DETAILS_MAPPER.toSkills(skills).stream()
                        .map(Skill::getName)
                        .collect(Collectors.toList());
        Mail mail = new Mail();
        mail.setSubject("Your new skills at Worksearch.com");
        mail.setEmail(email);
        String content = "";
        List<String> approvedSkillNames = getSkillNames.apply(approvedSkills);
        List<String> unapprovedSkillNames = getSkillNames.apply(unapprovedSkills);
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("email", email);
            model.put("isPresentApprovedSkills", approvedSkillNames.size() > 0);
            model.put("approvedSkills", approvedSkillNames);
            model.put("isPresentUnapprovedSkills",unapprovedSkillNames.size() > 0);
            model.put("unapprovedSkills", unapprovedSkillNames);
            Template template = freemarkerConfigurer.getConfiguration().getTemplate("add-new-skills-message.ftl");
            content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (IOException | TemplateException e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
        mail.setMessage(content);
        emailService.sendHtmlMessage(mail);
    }
}
