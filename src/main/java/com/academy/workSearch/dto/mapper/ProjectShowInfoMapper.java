package com.academy.workSearch.dto.mapper;

import com.academy.workSearch.dto.ProjectDTO;
import com.academy.workSearch.dto.ProjectShowInfoDTO;
import com.academy.workSearch.model.Project;
import com.academy.workSearch.model.enums.ProjectStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE)
public interface ProjectShowInfoMapper {
    ProjectMapper PROJECT_MAPPER = Mappers.getMapper(ProjectMapper.class);

    ProjectShowInfoDTO toDto(Project project);

    Project toEntity(ProjectShowInfoDTO projectDto);

    List<Project> toProjects(List<ProjectShowInfoDTO> projectDtos);

    List<ProjectShowInfoDTO> toProjectsDtos(List<ProjectDTO> projectDtos);

    @Named("projectStatusStr")
    default String projectStatusStr(ProjectStatus projectStatus) {
        return projectStatus.toString();
    }

    @Named("projectStatus")
    default ProjectStatus projectStatus(String projectStatus) {
        return ProjectStatus.valueOf(projectStatus);
    }

}
