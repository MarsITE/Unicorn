package com.academy.workSearch.dto.mapper;

import com.academy.workSearch.dto.UserRegistrationDto;
import com.academy.workSearch.model.User;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

public interface UserRegistrationMapper {
    UserRegistrationMapper USER_REGISTRATION_MAPPER = Mappers.getMapper(UserRegistrationMapper.class);

            @Mapping(target = "email", source = "email")
            @Mapping(target = "password", source = "password")
    UserRegistrationDto toDto(User user);

            @Mapping(target = "email", source = "email")
            @Mapping(target = "password", source = "password")
    User toEntity(UserRegistrationDto userRegistrationDto);


}


