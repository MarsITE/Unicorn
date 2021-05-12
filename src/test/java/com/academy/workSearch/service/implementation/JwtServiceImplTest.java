package com.academy.workSearch.service.implementation;

import com.academy.workSearch.dao.RoleDAO;
import com.academy.workSearch.model.Role;
import com.academy.workSearch.model.User;
import com.academy.workSearch.model.UserInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtServiceImplTest {
    @InjectMocks
    private JwtServiceImpl jwtService;

    @Mock
    private RoleDAO roleDAO;

    @Test
    void checkGeneratingAccessToken() {
        User user = new User();
        user.setUserId(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f7"));
        UserInfo userInfo = new UserInfo();
        userInfo.setUserInfoId(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f8"));
        user.setUserInfo(userInfo);
        user.setEmail("a@gmail.com");
        Set<Role> roles = new HashSet<>();
        Role worker = new Role(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f4"), "WORKER");
        roles.add(worker);
        user.setRoles(roles);

        when(roleDAO.getByName(any())).thenReturn(Optional.of(worker));

        assertNotNull(jwtService.generateAccessToken(user), "Token is not create");
    }

    @Test
    void checkGeneratingRefreshsToken() {
        assertNotNull(jwtService.generateRefreshToken("a@gmail.com"), "Token is not create");
    }

    @Test
    void checkGeneratingRegistrationToken() {
        assertNotNull(jwtService.generateRegistrationToken("a@gmail.com"), "Token is not create");
    }


    @Test
    void getIdFromToken() {
        User user = new User();
        user.setUserId(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f7"));
        UserInfo userInfo = new UserInfo();
        userInfo.setUserInfoId(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f8"));
        user.setUserInfo(userInfo);
        user.setEmail("a@gmail.com");
        Set<Role> roles = new HashSet<>();
        Role worker = new Role(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f4"), "WORKER");
        roles.add(worker);
        user.setRoles(roles);

        when(roleDAO.getByName(any())).thenReturn(Optional.of(worker));

        String token = jwtService.generateAccessToken(user);

        assertEquals("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f7", jwtService.getUserId(token), "bad token");
    }

    @Test
    void getEmailFromToken() {
        User user = new User();
        user.setUserId(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f7"));
        UserInfo userInfo = new UserInfo();
        userInfo.setUserInfoId(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f8"));
        user.setUserInfo(userInfo);
        user.setEmail("a@gmail.com");
        Set<Role> roles = new HashSet<>();
        Role worker = new Role(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f4"), "WORKER");
        roles.add(worker);
        user.setRoles(roles);

        when(roleDAO.getByName(any())).thenReturn(Optional.of(worker));

        String token = jwtService.generateAccessToken(user);

        assertEquals("a@gmail.com", jwtService.getUsername(token), "bad token");
    }

    @Test
    void getRoles() {
        User user = new User();
        user.setUserId(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f7"));
        UserInfo userInfo = new UserInfo();
        userInfo.setUserInfoId(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f8"));
        user.setUserInfo(userInfo);
        user.setEmail("a@gmail.com");
        Set<Role> roles = new HashSet<>();
        Role worker = new Role(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f4"), "WORKER");
        roles.add(worker);
        user.setRoles(roles);

        when(roleDAO.getByName(any())).thenReturn(Optional.of(worker));

        String token = jwtService.generateAccessToken(user);

        assertEquals(roles.size(), jwtService.getRoles(token).size(), "bad token");
    }

}
