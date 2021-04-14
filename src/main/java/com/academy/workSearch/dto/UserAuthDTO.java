package com.academy.workSearch.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@RedisHash("jwt")
public class UserAuthDTO {
    private String email;
    private String accessToken;
    private String refreshToken;
}
