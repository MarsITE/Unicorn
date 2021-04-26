package com.academy.workSearch.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserRegistrationDTO {
    private String email;
    private String password;
    private Boolean isEmployer;
}
