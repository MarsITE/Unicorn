package com.academy.workSearch.controller.jwt;

import com.academy.workSearch.dao.RoleDAO;
import com.academy.workSearch.dto.UserDTO;
import com.academy.workSearch.exceptionHandling.ExpiredJwtException;
import com.academy.workSearch.model.Role;
import com.academy.workSearch.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.*;

@Component
public class TokenProvider {
    private final Logger logger = LogManager.getLogger(TokenProvider.class);
    private final Map<String, Object> tokenData = new HashMap<>();
    private final String secret = "abc123";
    private final static Calendar EXPIRATION_DATE = Calendar.getInstance();
    private final RoleDAO roleDAO;

    public TokenProvider(RoleDAO roleDAO) {
        EXPIRATION_DATE.set(Calendar.MINUTE, 10);
        tokenData.put("token_expiration_date", EXPIRATION_DATE.getTime());
        this.roleDAO = roleDAO;
    }

    public String getToken(User user) {
        tokenData.put("user_id", user.getUserId().toString());
        tokenData.put("email", user.getEmail());
        tokenData.put("token_create_date", LocalDate.now());
        if (user.getUserInfo() != null) {
            if(user.getUserInfo().getFirstName() != null) {
                tokenData.put("first_name", user.getUserInfo().getFirstName());
            }
            if(user.getUserInfo().getLastName() != null) {
                tokenData.put("last_name", user.getUserInfo().getLastName());
            }
        }
        putRolesToToken(tokenData, user.getRoles());

        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setExpiration(EXPIRATION_DATE.getTime());
        jwtBuilder.setClaims(tokenData);
        return jwtBuilder.signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    private void putRolesToToken(Map<String, Object> tokenData, Set<Role> roles) {
        roles.forEach(role -> {
            switch (role.getName()) {
                case "ADMIN":
                    tokenData.put("isAdmin", roleDAO.getByName("ADMIN").getName());
                    break;
                case "EMPLOYER":
                    tokenData.put("isEmployer", roleDAO.getByName("EMPLOYER").getName());
                    break;
                default:
                    tokenData.put("isWorker", roleDAO.getByName("WORKER").getName());
                    break;
            }
        });
    }

    public String doGenerateRefreshToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(EXPIRATION_DATE.getTime())
                .signWith(SignatureAlgorithm.HS512, secret).compact();

    }

    public boolean validateToken(String authToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return !claims.getSignature().isEmpty();
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            throw new BadCredentialsException("Invalid credentials", ex);
        } catch (ExpiredJwtException ex) {
            throw new ExpiredJwtException("Token expired");
        }
    }

    public List<SimpleGrantedAuthority> getRolesFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

        List<SimpleGrantedAuthority> roles = null;

        Boolean isAdmin = claims.get("isAdmin", Boolean.class);
        Boolean isEmployer = claims.get("isEmployer", Boolean.class);
        Boolean isWorker = claims.get("isWorker", Boolean.class);

        if (isAdmin != null && isAdmin) {
            roles = Collections.singletonList(new SimpleGrantedAuthority(roleDAO.getByName("ADMIN").getName()));
        }

        if (isEmployer != null) {
            roles = Collections.singletonList(new SimpleGrantedAuthority(roleDAO.getByName("EMPLOYER").getName()));
        }

        if (isWorker != null) {
            roles = Collections.singletonList(new SimpleGrantedAuthority(roleDAO.getByName("WORKER").getName()));
        }
        return roles;

    }

    public static String decodeToken(String token) throws UnsupportedEncodingException {
        if (!token.isEmpty()) {
            String payload = token.split("\\.")[1];
            return new String(Base64.getDecoder().decode(payload), "UTF-8");
        }
        throw new UnsupportedEncodingException("Incorrect token");
    }


    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public UserDTO getUserDTOFromToken(String token) {
        if (!token.isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.readValue(token, UserDTO.class);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        return new UserDTO();
    }

}
