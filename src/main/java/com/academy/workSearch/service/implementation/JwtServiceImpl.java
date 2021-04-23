package com.academy.workSearch.service.implementation;

import com.academy.workSearch.dao.RoleDAO;
import com.academy.workSearch.exceptionHandling.exceptions.NoSuchEntityException;
import com.academy.workSearch.model.Role;
import com.academy.workSearch.model.User;
import com.academy.workSearch.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static com.academy.workSearch.exceptionHandling.MessageConstants.NO_ROLE;

@Service
public class JwtServiceImpl implements JwtService {
    private final RoleDAO roleDAO;

    private final String SECRET_KEY = "secret";
    private static final long EXPIRATION_TIME_ACCESS_TOKEN = 3600000 * 24; // 1 hour
    private static final long EXPIRATION_TIME_REGISTRATION_TOKEN = 3600000 * 24; // 1 day

    public JwtServiceImpl(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
        this.roleDAO.setClazz(Role.class);
    }

    public String extraUsername(String token) {
        return extraClaim(token, Claims::getSubject);
    }

    public Date extraExpiration(String token) {
        return extraClaim(token, Claims::getExpiration);
    }

    public <T> T extraClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extraAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extraAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token, Date date) {
        return extraExpiration(token).before(date);
    }

    @Override
    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getUserId());
        setRoles(claims, user.getRoles());
        return createAccessToken(claims, user.getEmail());
    }

    @Override
    public String generateRegistrationToken(String email) {
        return createRegistrationToken(email);
    }

    @Override
    public String generateRefreshToken(String email) {
        return createRefreshToken(new HashMap<>(), email);
    }

    private String createAccessToken(Map<String, Object> claims, String username) {
        return Jwts.builder().setClaims(claims).setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_ACCESS_TOKEN))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    private String createRefreshToken(Map<String, Object> claims, String username) {
        return Jwts.builder().setClaims(claims).setSubject(username)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    private String createRegistrationToken(String username) {
        return Jwts.builder().setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_REGISTRATION_TOKEN))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    private void setRoles(Map<String, Object> claims, Set<Role> roles) {
        roles.forEach(role -> {
            switch (role.getName()) {
                case "ADMIN":
                    claims.put("isAdmin", roleDAO.getByName("ADMIN")
                            .orElseThrow(() -> new NoSuchEntityException(NO_ROLE + "ADMIN")));
                    break;
                case "EMPLOYER":
                    claims.put("isEmployer", roleDAO.getByName("EMPLOYER")
                            .orElseThrow(() -> new NoSuchEntityException(NO_ROLE + "EMPLOYER")));
                    break;
                case "WORKER":
                    claims.put("isWorker", roleDAO.getByName("WORKER")
                            .orElseThrow(() -> new NoSuchEntityException(NO_ROLE + "WORKER")));
                    break;
                default: break;
            }
        });
    }

    @Override
    public boolean validateAccessToken(String token, UserDetails userDetails) {
        return (extraUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token, new Date()));
    }

    @Override
    public boolean validateRefreshToken(String token, String email) {
        return (extraUsername(token).equals(email));
    }

    @Override
    public boolean isRegistrationTokenNotExpired(String token) {
        try {
            return !isTokenExpired(token, new Date(EXPIRATION_TIME_REGISTRATION_TOKEN));
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(null, null, "This token is expired!");
        }
    }

}
