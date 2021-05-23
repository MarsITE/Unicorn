package com.academy.workSearch.dto.mapper;

import com.academy.workSearch.dto.ProjectDTO;
import com.academy.workSearch.dto.ProjectWorkerDTO;
import com.academy.workSearch.dto.WorkerInfoDTO;
import com.academy.workSearch.model.Project;
import com.academy.workSearch.model.ProjectUserInfo;
import com.academy.workSearch.model.Skill;
import com.academy.workSearch.model.User;
import com.academy.workSearch.model.UserInfo;
import com.academy.workSearch.model.enums.ProjectStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE, uses = {UserMapper.class, SkillMapper.class})
public interface ProjectMapper {

    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    /**
     * Keep updated with {@link ProjectShowInfoMapper#toWorkerDto}
     * @param project
     * @return
     */
    @Mappings({@Mapping(target = "id", source = "projectId"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "projectStatus", qualifiedByName = "projectStatusStr"),
            @Mapping(target = "creationDate", source = "creationDate"),
            @Mapping(target = "ownerId", source = "employer", qualifiedByName = "ownerId"),
            @Mapping(target = "ownerEmail", source = "employer", qualifiedByName = "ownerEmail"),
            @Mapping(target = "skills", source = "skills")
    })
    ProjectDTO toDto(Project project);

    @Mappings({@Mapping(target = "projectId", source = "id"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "projectStatus", qualifiedByName = "projectStatus"),
            @Mapping(target = "creationDate", source = "creationDate"),
            @Mapping(target = "employer", source = "ownerId", qualifiedByName = "owner"),
            @Mapping(target = "skills", source = "skills")})
    Project toEntity(ProjectDTO projectDto);

    List<Project> toProjects(List<ProjectDTO> projectDtos);

    @Mapping(target = "skills", qualifiedByName = "skillsStr")
    List<ProjectDTO> toProjectsDto(List<Project> projects);

    default WorkerInfoDTO mapWorkers(ProjectUserInfo worker) {
        WorkerInfoDTO workerInfoDTO = new WorkerInfoDTO();
        workerInfoDTO.setEmail(worker.getUserInfo().getUser().getEmail());
        workerInfoDTO.setApprove(worker.isApprove());
        return workerInfoDTO;
    }

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

    @Named("projectStatusStr")
    default String projectStatusStr(ProjectStatus projectStatus) {
        return projectStatus.toString();
    }

    @Named("projectStatus")
    default ProjectStatus projectStatus(String projectStatus) {
        return ProjectStatus.valueOf(projectStatus);
    }

    @Named("ownerId")
    default String ownerId(User user) {
        return user.getUserId().toString();
    }

    @Named("ownerEmail")
    default String ownerEmail(User user) {
        return user.getEmail();
    }

    @Named("owner")
    default User owner(String ownerId) {
        User user;
        if (ownerId == null) {
            user = null;
        } else {
            user = new User();
            user.setUserId(UUID.fromString(ownerId));
        }
        return user;
    }

   default ProjectWorkerDTO toProjectWorkerDTO(ProjectUserInfo projectUserInfo) {
       ProjectWorkerDTO projectWorkerDTO = new ProjectWorkerDTO();
       projectWorkerDTO.setUserInfoProjectId(projectUserInfo.getUserInfoProjectId());
       projectWorkerDTO.setApprove(projectUserInfo.isApprove());
       UserInfo userInfo = projectUserInfo.getUserInfo();
       projectWorkerDTO.setFirstName(userInfo.getFirstName());
       projectWorkerDTO.setLastName(userInfo.getLastName());
       projectWorkerDTO.setEmail(userInfo.getUser().getEmail());
       projectWorkerDTO.setId(userInfo.getUser().getUserId());
       return projectWorkerDTO;
   }
}
