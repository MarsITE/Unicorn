package com.academy.workSearch.service;

import com.academy.workSearch.dao.CrudDAO;
import com.academy.workSearch.model.Skill;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class SkillService {

    @Autowired
    private final CrudDAO<Skill> skillDAO;

    public List<Skill> findAll() {
        return skillDAO.findAll();
    }

    public boolean save(Skill skill) {
        skillDAO.save(skill);
    }

    public Skill get(UUID id) {
        return skillDAO.get(id);
    }

    public void delete(UUID id) {
        skillDAO.delete(id);
    }

    public boolean confirmSkill(Skill skills){
        StringBuilder error = new StringBuilder();
        boolean isValidSkills = true;
        for (Skill skill: skills) {
            if (!save(skill)) {
                error.append(skill.getName());
                isValidSkills=false;
            }
        }
        if(!isValidSkills){
            //exeption
            return false;
        }

        return true;
    }
}
