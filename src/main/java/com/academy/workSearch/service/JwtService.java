package com.academy.workSearch.service;

import com.academy.workSearch.model.Role;
import com.academy.workSearch.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;
import java.util.UUID;

public interface JwtService {

    boolean isValidAccessToken(String token, UserDetails userDetails);

    boolean isValidRefreshToken(String token, String email);

    boolean isRegistrationTokenNotExpired(String token);

    String generateRefreshToken(String email);

    String generateAccessToken(User user);

    String generateRegistrationToken(String email);

    String getUsername(String token);

    Set<Role> getRoles(String token);

    String getUserId(String token);

    UUID getUserInfoId(String token);

}
