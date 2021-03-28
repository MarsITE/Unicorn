package com.academy.workSearch.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserAuthDTO {
    private String email;
    private String password;
    private Set<String> roles;
}
