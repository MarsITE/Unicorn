package com.academy.workSearch.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter

public class UserDTO {
    private String email;
    private Set<String> roles;
    private String accountStatus;

}
