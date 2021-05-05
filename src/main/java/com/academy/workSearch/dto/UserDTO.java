package com.academy.workSearch.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter

public class UserDTO {
    private UUID userId;
    private String email;
    private Set<String> roles;
    private String accountStatus;
    private UserInfoDTO userInfo;


}
