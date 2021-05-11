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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

import static com.academy.workSearch.exceptionHandling.MessageConstants.NO_ROLE;

@Service
public class JwtServiceImpl implements JwtService {
    private final Logger logger = LogManager.getLogger(JwtServiceImpl.class);
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
    public boolean isValidAccessToken(String token, UserDetails userDetails) {
        return getUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token, new Date());
    }

    /**
     * @param token refresh
     * @param email authorizedUser
     * @return true if token valid
     */
    @Override
    public boolean isValidRefreshToken(String token, String email) {
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
        return claimResolver.apply(getClaims(token));
    }

    /**
     * @param token access
     * @return get token's body
     */
    private Claims getClaims(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            logger.info("This token is expired!");
        }
        return claims;
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
     * @param token acess
     * @return get set roles
     */
    @Override
    public Set<Role> getRoles(String token) {
        Claims claims = getClaims(token);

        Set<Role> roles = new HashSet<>();
        if (claims != null) {
            if (claims.get("isAdmin") != null) {
                roles.add(getRoleByName("ADMIN"));
            }
            if (claims.get("isEmployer") != null) {
                roles.add(getRoleByName("EMPLOYER"));
            }
            if (claims.get("isWorker") != null) {
                roles.add(getRoleByName("WORKER"));
            }
        }
        return roles;
    }

    /**
     * @param token access
     * @return id of current user
     */
    @Override
    public String getUserId(String token) {
        Claims claims = getClaims(token);

        if (claims != null) {
            if (claims.get("id") != null) {
                return claims.get("id").toString();
            }
        }
        return "";
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
                    claims.put("isAdmin", getRoleByName("ADMIN"));
                    break;
                case "EMPLOYER":
                    claims.put("isEmployer", getRoleByName("EMPLOYER"));
                    break;
                case "WORKER":
                    claims.put("isWorker", getRoleByName("WORKER"));
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * @param name of role
     * @return get role from db
     */
    private Role getRoleByName(String name) {
        return roleDAO.getByName(name)
                .orElseThrow(() -> new NoSuchEntityException(NO_ROLE + name));
    }
}
