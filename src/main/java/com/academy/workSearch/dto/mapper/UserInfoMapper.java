package com.academy.workSearch.dto.mapper;

import com.academy.workSearch.dto.ProjectShowInfoDTO;
import com.academy.workSearch.dto.UserInfoDTO;
import com.academy.workSearch.model.Skill;
import com.academy.workSearch.model.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE, uses = {ProjectShowInfoDTO.class})
public interface UserInfoMapper {

    UserInfoMapper USER_INFO_MAPPER = Mappers.getMapper(UserInfoMapper.class);

    @Mapping(target = "userInfoId", qualifiedByName = "uuid")
    @Mapping(target = "skills", source = "skills", qualifiedByName = "skillsSet")
    UserInfo toUserInfo(UserInfoDTO userInfoDto);

    @Mapping(target = "userInfoId", qualifiedByName = "uuidStr")
    @Mapping(target = "skills", source = "skills", qualifiedByName = "skillsStr")
    UserInfoDTO toUserInfoDto(UserInfo userInfo);

    @Named("uuidStr")
    default String idStr(UUID userInfoId) {
        return userInfoId.toString();
    }

    @Named("uuid")
    default UUID uuid(String userInfoId) {
        return UUID.fromString(userInfoId);
    }

    @Named("skillsStr")
    default Set<String> skillsStr(Set<Skill> skills) {
        Set<String> skillsStr = new HashSet<>();
        skills.forEach(skill -> skillsStr.add(skill.getName()));
        return skillsStr;
    }

    @Named("skillsSet")
    default Set<Skill> skills(Set<String> skillsStr) {
        Set<Skill> skills = new HashSet<>();
        skillsStr.forEach(skill -> {
            Skill s = new Skill();
            s.setName(skill);
            skills.add(s);
        });

        return skills;
    }
}
