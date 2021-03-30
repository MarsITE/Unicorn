package com.academy.workSearch.dto.mapper;

import com.academy.workSearch.dto.UserAuthDTO;
import com.academy.workSearch.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE)
public interface UserAuthMapper {
    UserAuthMapper USER_AUTH_MAPPER = Mappers.getMapper(UserAuthMapper.class);

    User toUser(UserAuthDTO userAuthDTO);

    UserAuthDTO toUserAuthDto(User user);
}
