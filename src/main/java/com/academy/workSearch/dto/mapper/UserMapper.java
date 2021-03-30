package com.academy.workSearch.dto.mapper;

import com.academy.workSearch.dto.UserDTO;
import com.academy.workSearch.model.Role;
import com.academy.workSearch.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE, uses = {UserInfoMapper.class})
public interface UserMapper {
    UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "roles", qualifiedByName = "roles")
    User toUser(UserDTO userDTO);

    @Mapping(target = "roles", qualifiedByName = "rolesStr")
    UserDTO toUserDto(User user);

    @Mapping(target = "roles", qualifiedByName = "rolesStr")
    List<UserDTO> map(List<User> users);

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
