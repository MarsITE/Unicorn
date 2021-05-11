package com.academy.workSearch.service.implementation;

import com.academy.workSearch.dao.RoleDAO;
import com.academy.workSearch.dao.UserDAO;
import com.academy.workSearch.dao.UserInfoDAO;
import com.academy.workSearch.dto.UserAuthDTO;
import com.academy.workSearch.dto.UserRegistrationDTO;
import com.academy.workSearch.model.Role;
import com.academy.workSearch.model.User;
import com.academy.workSearch.model.UserInfo;
import com.academy.workSearch.model.enums.AccountStatus;
import com.academy.workSearch.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static com.academy.workSearch.dto.mapper.UserMapper.USER_MAPPER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDAO userDAO;
    @Mock
    private UserInfoDAO userInfoDAO;
    @Mock
    private RoleDAO roleDAO;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void init() {
        userDAO.setClazz(User.class);
    }


    @Test
    void findAll() {
        when(userDAO.findAll()).thenReturn(Arrays.asList(new User(), new User(), new User()));

        assertEquals(3, userService.findAll().size());
    }

    @Test
    void findById() {
        User user = new User();
        user.setUserId(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f4"));
        user.setEmail("anna@gmail.com");
        when(userDAO.get(user.getUserId())).thenReturn(Optional.of(user));

        assertSame("anna@gmail.com", userService.get(user.getUserId()).getEmail(), "The user returned was not the same as the mock");
    }

    @Test
    void save() {
        User user = new User();
        user.setEmail("anna@gmail.com");
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f4"), "WORKER"));
        roles.add(new Role(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb1cb5f4"), "EMPLOYER"));
        user.setRoles(roles);
        UserInfo userInfo = new UserInfo();
        userInfo.setShowInfo(true);
        user.setPassword("$2a$10$8R//8mSYBIn4hpFM2pV8k.Ye7nPCDQNXtDGZz7FOsxjK.U7B6RYpq");
        user.setRegistrationToken("uyvvdbunisndbhvfbnjdwmjhvfbjdnmvbdfnwdm");
        user.setAccountStatus(AccountStatus.NOT_ACTIVE);

        when(userDAO.save(any())).thenReturn(user);
        when(roleDAO.getByName("WORKER")).thenReturn(Optional.of(new Role(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f4"), "WORKER")));
        when(roleDAO.getByName("EMPLOYER")).thenReturn(Optional.of(new Role(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb1cb5f4"), "EMPLOYER")));
        when(userInfoDAO.saveAndGetId(userInfo)).thenReturn(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb2cb5f4"));
        when(passwordEncoder.encode("111111")).thenReturn("$2a$10$8R//8mSYBIn4hpFM2pV8k.Ye7nPCDQNXtDGZz7FOsxjK.U7B6RYpq");
        when(jwtService.generateRegistrationToken(user.getEmail())).thenReturn("uyvvdbunisndbhvfbnjdwmjhvfbjdnmvbdfnwdm");

        UserServiceImpl userServiceImpl = spy(userService);
        doNothing().when(userServiceImpl).sendMessageWithActivationLink(any());

        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setEmail("anna@gmail.com");
        userRegistrationDTO.setPassword("111111");
        userRegistrationDTO.setIsEmployer(true);
        UserAuthDTO expected = userServiceImpl.save(userRegistrationDTO);

        assertEquals("anna@gmail.com", expected.getEmail(), "The user returned was not the same as the mock");
    }

    @Test
    void update() {
        User user = new User();
        user.setEmail("admin@gmail.com");
        user.setUserId(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb1cb5f4"));

        when(userDAO.getByEmail("admin@gmail.com")).thenReturn(Optional.of(user));
        when(userDAO.save(any())).thenReturn(user);

        assertEquals("admin@gmail.com", userService.update(USER_MAPPER.toUserDto(user)).getEmail(), "The email returned was not the same as the mock");
    }

    @Test
    void delete() {
        User user = new User();
        user.setEmail("admin@gmail.com");
        user.setUserId(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb1cb5f4"));

        when(userDAO.delete(any())).thenReturn(user);

        assertEquals(user.getEmail(), userService.delete(user.getUserId()).getEmail(), "The user returned was not the same as the mock");
    }

    @Test
    void isVerifyAccount() {
        User user = new User();
        user.setEmail("admin@gmail.com");
        user.setUserId(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb1cb5f4"));

        when(userDAO.getByToken(anyString())).thenReturn(Optional.of(user));
        when(userDAO.save(user)).thenReturn(user);
        when(jwtService.isRegistrationTokenNotExpired(anyString())).thenReturn(true);

        assertTrue(userService.isVerifyAccount("token"), "Account isn't verified");
    }
}