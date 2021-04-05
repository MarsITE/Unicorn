package com.academy.workSearch.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserAuthDTO {
    private String email;
    private String token;
}
