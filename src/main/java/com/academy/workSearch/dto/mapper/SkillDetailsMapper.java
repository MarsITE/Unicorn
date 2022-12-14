package com.academy.workSearch.dto.mapper;
import com.academy.workSearch.dto.SkillDetailsDTO;
import com.academy.workSearch.model.Skill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper
public interface SkillDetailsMapper {
    SkillDetailsMapper SKILL_DETAILS_MAPPER = Mappers.getMapper(SkillDetailsMapper.class);

    @Mappings({
            @Mapping(target = "skillId", source = "skillId"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "enabled", source = "enabled")
    })
    SkillDetailsDTO toDto(Skill skill);

    @Mappings({
            @Mapping(target = "skillId", source = "skillId"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "enabled", source = "enabled")
    })
    Skill toEntity(SkillDetailsDTO skillDto);

    List<SkillDetailsDTO> toSkillsDto(List<Skill> skills);

    List<Skill> toSkills(List<SkillDetailsDTO> toSkillsDto);
}
