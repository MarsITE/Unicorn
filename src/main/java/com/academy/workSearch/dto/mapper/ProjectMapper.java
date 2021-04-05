package com.academy.workSearch.dto.mapper;

import com.academy.workSearch.dto.ProjectDTO;
import com.academy.workSearch.model.Project;
import com.academy.workSearch.model.Skill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE, uses = {UserMapper.class, SkillMapper.class})
public interface ProjectMapper {

    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    @Mappings({ @Mapping(target = "id", source = "projectId"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "projectStatus", source = "projectStatus"),
            @Mapping(target = "creationDate", source = "creationDate"),
            @Mapping(target = "owner", source = "employer"),
            @Mapping(target = "skills", qualifiedByName = "skillsStr")
    })
    ProjectDTO toDto(Project project);

    @Mappings({ @Mapping(target = "projectId", source = "id"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "projectStatus", source = "projectStatus"),
            @Mapping(target = "creationDate", source = "creationDate"),
            @Mapping(target = "employer", source = "owner"),
            @Mapping(target = "skills", qualifiedByName = "skills")})
    Project toEntity(ProjectDTO projectDto);

    List<Project> toProjects(List<ProjectDTO> projectDtos);

    @Mapping(target = "skills", qualifiedByName = "skillsStr")
    List<ProjectDTO> toProjectsDto(List<Project> projects);

    @Named("skillsStr")
    default Set<String> skillsStr(Set<Skill> skills) {
        return skills.stream().map(Skill::getName).collect(Collectors.toSet());
    }

    @Named("skills")
    default Set<Skill> skills(Set<String> skillsStr) {
        Set<Skill> skills = new HashSet<>();
        skillsStr.forEach(r -> {
            Skill skill = new Skill();
            skill.setName(r);
            skills.add(skill);
        });
        return skills;
    }


}
