package com.academy.workSearch.dto.mapper;

import com.academy.workSearch.dto.SkillDTO;
import com.academy.workSearch.dto.UserInfoDTO;
import com.academy.workSearch.model.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE, uses = {SkillDTO.class})
public interface UserInfoMapper {

    UserInfoMapper USER_INFO_MAPPER = Mappers.getMapper(UserInfoMapper.class);

    @Mapping(target = "userInfoId", qualifiedByName = "uuid")
    UserInfo toUserInfo(UserInfoDTO userInfoDto);

    @Mapping(target = "userInfoId", qualifiedByName = "uuidStr")
    UserInfoDTO toUserInfoDto(UserInfo userInfo);

    @Named("uuidStr")
    default String idStr(UUID userInfoId) {
        return userInfoId.toString();
    }

    @Named("uuid")
    default UUID uuid(String userInfoId) {
        return UUID.fromString(userInfoId);
    }
}
