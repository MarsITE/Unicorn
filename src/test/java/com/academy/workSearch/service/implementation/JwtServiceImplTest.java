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

import java.util.*;

import static com.academy.workSearch.service.RedisService.KEY_REFRESH_TOKEN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtServiceImplTest {
    @InjectMocks
    private JwtServiceImpl jwtService;

    @Mock
    private RoleDAO roleDAO;

    @Mock
    private RedisServiceImpl redisService;

    @Test
    void checkGeneratingAccessToken() {
        User user = new User();
        user.setUserId(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f7"));
        UserInfo userInfo = new UserInfo();
        userInfo.setUserInfoId(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f8"));
        user.setUserInfo(userInfo);
        user.setEmail("a@gmail.com");
        Role worker = new Role(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f4"), "WORKER");
        Set<Role> roles = new HashSet<>(Collections.singletonList(worker));
        user.setRoles(roles);

        when(roleDAO.getByName(any())).thenReturn(Optional.of(worker));

        assertNotNull(jwtService.generateAccessToken(user), "Token is not create");
    }

    @Test
    void checkGeneratingRefreshToken() {
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
    void getRoleWorker() {
        User user = new User();
        user.setUserId(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f7"));
        UserInfo userInfo = new UserInfo();
        userInfo.setUserInfoId(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f8"));
        user.setUserInfo(userInfo);
        user.setEmail("a@gmail.com");
        Role worker = new Role(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f4"), "WORKER");
        Set<Role> roles = new HashSet<>(Collections.singletonList(worker));
        user.setRoles(roles);

        when(roleDAO.getByName(any())).thenReturn(Optional.of(worker));

        String token = jwtService.generateAccessToken(user);

        assertEquals(roles.size(), jwtService.getRoles(token).size(), "bad token");
    }

    @Test
    void getRoleEmployer() {
        User user = new User();
        user.setUserId(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f7"));
        UserInfo userInfo = new UserInfo();
        userInfo.setUserInfoId(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f8"));
        user.setUserInfo(userInfo);
        user.setEmail("a@gmail.com");
        Role employer = new Role(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f2"), "EMPLOYER");
        Set<Role> roles = new HashSet<>(Collections.singletonList(employer));
        user.setRoles(roles);

        when(roleDAO.getByName(any())).thenReturn(Optional.of(employer));

        String token = jwtService.generateAccessToken(user);

        assertEquals(roles.size(), jwtService.getRoles(token).size(), "bad token");
    }

    @Test
    void getRoleAdmin() {
        User user = new User();
        user.setUserId(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f7"));
        UserInfo userInfo = new UserInfo();
        userInfo.setUserInfoId(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f8"));
        user.setUserInfo(userInfo);
        user.setEmail("a@gmail.com");
        Role admin = new Role(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f1"), "ADMIN");
        Set<Role> roles = new HashSet<>(Collections.singletonList(admin));
        user.setRoles(roles);

        when(roleDAO.getByName(any())).thenReturn(Optional.of(admin));

        String token = jwtService.generateAccessToken(user);

        assertEquals(roles.size(), jwtService.getRoles(token).size(), "bad token");
    }


    @Test
    public void checkUserInfoId() {
        User user = new User();
        user.setUserId(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f7"));
        UserInfo userInfo = new UserInfo();
        userInfo.setUserInfoId(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f7"));
        user.setUserInfo(userInfo);
        user.setEmail("a@gmail.com");
        Role admin = new Role(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f1"), "ADMIN");
        Set<Role> roles = new HashSet<>(Collections.singletonList(admin));
        user.setRoles(roles);

        when(roleDAO.getByName(any())).thenReturn(Optional.of(admin));

        String token = jwtService.generateAccessToken(user);

        assertEquals(userInfo.getUserInfoId(), jwtService.getUserInfoId(token), "bad token");
    }

    @Test
    void checkIfUserInfoIdEmpty() {
        User user = new User();
        user.setUserId(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f7"));
        UserInfo userInfo = new UserInfo();
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
            userInfo.setUserInfoId(UUID.fromString(""));
        });

        assertEquals("Invalid UUID string: ", exception1.getMessage(), "userInfoId correct");
        user.setUserInfo(userInfo);
        user.setEmail("a@gmail.com");
        Role admin = new Role(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f1"), "ADMIN");
        Set<Role> roles = new HashSet<>(Collections.singletonList(admin));
        user.setRoles(roles);

        when(roleDAO.getByName(any())).thenReturn(Optional.of(admin));

        String token = jwtService.generateAccessToken(user);

        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            jwtService.getUserInfoId(token);
        });

        assertNull(exception2.getMessage(), "userInfoId correct");

    }
    @Test
    void checkIfUserIdEmpty() {
        User user = new User();
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
            user.setUserId(UUID.fromString(""));
        });
        assertEquals("Invalid UUID string: ", exception1.getMessage(), "userInfoId correct");
        UserInfo userInfo = new UserInfo();
        userInfo.setUserInfoId(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f7"));
        user.setUserInfo(userInfo);
        user.setEmail("a@gmail.com");
        Role admin = new Role(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f1"), "ADMIN");
        Set<Role> roles = new HashSet<>(Collections.singletonList(admin));
        user.setRoles(roles);

        when(roleDAO.getByName(any())).thenReturn(Optional.of(admin));

        String token = jwtService.generateAccessToken(user);

        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            jwtService.getUserId(token);
        });

        assertNull(exception2.getMessage(), "userInfoId correct");
    }

    @Test
    void getExpiration() {
        String token = jwtService.generateRegistrationToken("a@gmail.com");

        assertTrue(jwtService.isRegistrationTokenNotExpired(token), "token expired");
    }

    @Test
    void isValidRefreshToken() {
        String email = "a@gmail.com";
        String token = jwtService.generateRegistrationToken(email);
        when(redisService.getValue(KEY_REFRESH_TOKEN)).thenReturn(token);
        assertTrue(jwtService.isValidRefreshToken(token, email), "token is not valid");
    }

    @Test
    void isValidAccessToken() {
        User user = new User();
        user.setUserId(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f7"));
        UserInfo userInfo = new UserInfo();
        userInfo.setUserInfoId(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f7"));
        user.setUserInfo(userInfo);
        user.setEmail("a@gmail.com");
        Role admin = new Role(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f1"), "ADMIN");
        Set<Role> roles = new HashSet<>(Collections.singletonList(admin));
        user.setRoles(roles);

        when(roleDAO.getByName(any())).thenReturn(Optional.of(admin));

        String token = jwtService.generateAccessToken(user);
        assertTrue(jwtService.isValidAccessToken(token, user));
    }

}
