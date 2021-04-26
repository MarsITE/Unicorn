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
    private static final long EXPIRATION_TIME_ACCESS_TOKEN = 3600000; // 1 hour
    private static final long EXPIRATION_TIME_REGISTRATION_TOKEN = 3600000 * 24; // 1 day

    public JwtServiceImpl(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
        this.roleDAO.setClazz(Role.class);
    }

    /**
     * @param user entity, need mail, id and roles
     * @return accessToken
     */
    @Override
    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getUserId());
        setRoles(claims, user.getRoles());
        return createAccessToken(claims, user.getEmail());
    }

    /**
     * @param email authorizedUser
     * @return registrationToken
     */
    @Override
    public String generateRegistrationToken(String email) {
        return createRegistrationToken(email);
    }

    /**
     * @param email authorizedUser
     * @return refreshToken
     */
    @Override
    public String generateRefreshToken(String email) {
        return createRefreshToken(email);
    }

    /**
     * @param token       access
     * @param userDetails user
     * @return true if token valid
     */
    @Override
    public boolean validateAccessToken(String token, UserDetails userDetails) {
        return getUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token, new Date());
    }

    /**
     * @param token refresh
     * @param email authorizedUser
     * @return true if token valid
     */
    @Override
    public boolean validateRefreshToken(String token, String email) {
        return getUsername(token).equals(email);
    }

    /**
     * check if registration token is not expired
     *
     * @param token authorizedUser
     * @return true if not expired
     */
    @Override
    public boolean isRegistrationTokenNotExpired(String token) {
        return !isTokenExpired(token, new Date(EXPIRATION_TIME_REGISTRATION_TOKEN));
    }

    /**
     * @param token         access
     * @param claimResolver for work with claims
     * @param <T>           your type
     * @return 1 option from token exp: id, roles
     */
    private <T> T getClaim(String token, Function<Claims, T> claimResolver) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(null, null, "This token is expired!");
        }
        return claimResolver.apply(claims);
    }

    /**
     * check if token is not expired
     *
     * @param token authorizedUser
     * @return true if not expired
     */
    private Boolean isTokenExpired(String token, Date date) {
        try {
            return getExpiration(token).before(date);
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(null, null, "This token is expired!");
        }
    }

    /**
     * @param token access
     * @return get email of user
     */
    public String getUsername(String token) {
        return getClaim(token, Claims::getSubject);
    }

    /**
     * @param token access
     * @return get expiration date
     */
    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    /**
     * @param claims   token body
     * @param username email
     * @return create access token
     */
    private String createAccessToken(Map<String, Object> claims, String username) {
        return Jwts.builder().setClaims(claims).setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_ACCESS_TOKEN))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    /**
     * @param username email
     * @return create refresh token
     */
    private String createRefreshToken(String username) {
        return Jwts.builder().setSubject(username)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    /**
     * @param username email
     * @return create registration token
     */
    private String createRegistrationToken(String username) {
        return Jwts.builder().setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_REGISTRATION_TOKEN))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    /**
     * @param claims token body
     * @param roles  roles of user
     *               add roles into claims
     */
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
                default:
                    break;
            }
        });
    }


}
