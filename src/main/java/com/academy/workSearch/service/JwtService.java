package com.academy.workSearch.service;

import com.academy.workSearch.model.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    boolean validateAccessToken(String token, UserDetails userDetails);

    boolean validateRefreshToken(String token, String email);

    String generateRefreshToken(String email);

    String generateAccessToken(User user);
}
