package com.academy.workSearch.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserLoginDTO {
    private String email;
    private String token;
    private boolean isLogged;
}
