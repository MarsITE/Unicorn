package com.academy.workSearch.dto.mapper;

import com.academy.workSearch.dto.ProjectDTO;
import com.academy.workSearch.model.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProjectMapper {

    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    @Mappings({ @Mapping(target = "id", source = "projectId"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "projectStatus", source = "projectStatus"),
            @Mapping(target = "creationDate", source = "creationDate"),
            @Mapping(target = "owner", source = "employer")})
    ProjectDTO toDto(Project project);

    @Mappings({ @Mapping(target = "projectId", source = "id"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "projectStatus", source = "projectStatus"),
            @Mapping(target = "creationDate", source = "creationDate"),
            @Mapping(target = "employer", source = "owner")})
    Project toEntity(ProjectDTO projectDto);

    List<Project> toProjects(List<ProjectDTO> projectDtos);

    List<ProjectDTO> toProjectsDto(List<Project> projects);

}
