package com.academy.workSearch.dto.mapper;

import com.academy.workSearch.dto.UserAuthDTO;
import com.academy.workSearch.model.Role;
import com.academy.workSearch.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE)
public interface UserAuthMapper {
    UserAuthMapper USER_AUTH_MAPPER = Mappers.getMapper(UserAuthMapper.class);

    @Mapping(target = "roles", qualifiedByName = "roles")
    User toUser(UserAuthDTO userAuthDTO);

    @Mapping(target = "roles", qualifiedByName = "rolesStr")
    UserAuthDTO toUserAuthDto(User user);

    @Named("rolesStr")
    default Set<String> rolesStr(Set<Role> roles) {
        return roles.stream().map(Role::getName).collect(Collectors.toSet());
    }

    @Named("roles")
    default Set<Role> roles(Set<String> rolesStr) {
        Set<Role> roles = new HashSet<>();
        rolesStr.forEach(r -> {
            Role role = new Role();
            role.setName(r);
            roles.add(role);
        });
        return roles;
    }
}
