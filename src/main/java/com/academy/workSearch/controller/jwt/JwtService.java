package com.academy.workSearch.controller.jwt;

import com.academy.workSearch.dao.RoleDAO;
import com.academy.workSearch.model.Role;
import com.academy.workSearch.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {
    private final RoleDAO roleDAO;

    private final String SECRET_KEY = "secret";
    private final long EXPIRATION_TIME = 3600000; // 1 hour

    public JwtService(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
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

    private Boolean isTokenExpires(String token) {
        return extraExpiration(token).before(new Date());
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getUserId());
        claims.put("firstname", user.getUserInfo().getFirstName());
        claims.put("lastname", user.getUserInfo().getLastName());
        setRoles(claims, user.getRoles());
        return createToken(claims, user.getEmail());
    }

    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    private void setRoles(Map<String, Object> claims, Set<Role> roles) {
        roles.forEach(role -> {
            switch (role.getName()) {
                case "ADMIN":
                    claims.put("isAdmin", roleDAO.getByName("ADMIN").getName());
                    break;
                case "EMPLOYER":
                    claims.put("isEmployer", roleDAO.getByName("EMPLOYER").getName());
                    break;
                default:
                    claims.put("isWorker", roleDAO.getByName("WORKER").getName());
                    break;
            }
        });

    }

    private void getRoles(String token) {
        Claims claims = extraAllClaims(token);

        List<SimpleGrantedAuthority> roles = null;

        Boolean isAdmin = claims.get("isAdmin", Boolean.class);
        Boolean isEmployer = claims.get("isEmployer", Boolean.class);
        Boolean isWorker = claims.get("isWorker", Boolean.class);


        if (isAdmin != null && isAdmin) {
            roles.add(new SimpleGrantedAuthority(roleDAO.getByName("ADMIN").getName()));
        }

        if (isEmployer != null) {
            roles.add(new SimpleGrantedAuthority(roleDAO.getByName("EMPLOYER").getName()));
        }

        if (isWorker != null) {
            roles.add(new SimpleGrantedAuthority(roleDAO.getByName("WORKER").getName()));
        }

    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extraUsername(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpires(token));
    }

}
