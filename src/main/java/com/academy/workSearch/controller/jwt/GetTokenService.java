package com.academy.workSearch.controller.jwt;

import com.academy.workSearch.dto.UserAuthDTO;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Component
public class GetTokenService {

    public String getToken(UserAuthDTO user) {
        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("clientType", "user");
        //tokenData.put("userID", user.getUserId().toString());
        tokenData.put("email", user.getEmail());
        tokenData.put("token_create_date", LocalDate.now());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 100);
        tokenData.put("token_expiration_date", calendar.getTime());

        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setExpiration(calendar.getTime());
        jwtBuilder.setClaims(tokenData);
        String key = "abc123";
        return jwtBuilder.signWith(SignatureAlgorithm.HS512, key).compact();
    }
}
