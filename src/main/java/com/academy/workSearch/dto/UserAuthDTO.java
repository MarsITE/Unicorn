package com.academy.workSearch.dto;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash("jwt_token")
public class UserAuthDTO {
    private String email;
    private String accessToken;
    private String refreshToken;
}
