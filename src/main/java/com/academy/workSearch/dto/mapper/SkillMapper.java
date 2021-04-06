package com.academy.workSearch.dto.mapper;

import com.academy.workSearch.dto.SkillDTO;
import com.academy.workSearch.model.Skill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper
public interface SkillMapper {
    SkillMapper SKILL_MAPPER = Mappers.getMapper(SkillMapper.class);


    @Mappings({
            @Mapping(target = "skillId", source = "skillId"),
            @Mapping(target = "name", source = "name")
    })
    SkillDTO toDto(Skill skill);

    @Mappings({
            @Mapping(target = "skillId", source = "skillId"),
            @Mapping(target = "name", source = "name")
    })
    Skill toEntity(SkillDTO skillDto);

    List<SkillDTO> toSkillsDto(List<Skill> skills);

    List<Skill> toSkills(List<SkillDTO> toSkillsDto);
}