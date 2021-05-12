package com.academy.workSearch.service.implementation;

import com.academy.workSearch.dao.RoleDAO;
import com.academy.workSearch.model.Role;
import com.academy.workSearch.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class JwtServiceImplTest {
    @InjectMocks
    private JwtServiceImpl jwtService;

    @Mock
    private RoleDAO roleDAO;

    @Test
    void checkGeneratingAccessToken() {
//        User user = new User();
//        user.setUserId(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f7"));
//        user.getUserInfo().setUserInfoId(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f8"));
//        user.setEmail("a@gmail.com");
//        Set<Role> roles = new HashSet<>();
//        roles.add(new Role(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f4"), "WORKER"));
//        roles.add(new Role(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb1cb5f4"), "EMPLOYER"));
//        user.setRoles(roles);
//
//
//        assertEquals("a@gmail.com", jwtService.generateAccessToken(user), "Token is not create");
    }
}
