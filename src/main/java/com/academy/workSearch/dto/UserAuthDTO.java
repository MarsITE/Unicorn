package com.academy.workSearch.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAuthDTO {
    private String email;
    private String accessToken;
    private String refreshToken;
}
