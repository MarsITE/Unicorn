package com.academy.workSearch.dto;

import lombok.Data;

@Data
public class UserAuthDTO {
    private String email;
    private String accessToken;
    private String refreshToken;
}
