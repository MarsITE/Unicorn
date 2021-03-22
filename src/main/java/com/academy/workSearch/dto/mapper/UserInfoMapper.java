package com.academy.workSearch.dto.mapper;

import com.academy.workSearch.dto.UserInfoDTO;
import com.academy.workSearch.model.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE)
public interface UserInfoMapper {

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
