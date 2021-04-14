package com.academy.workSearch.service.implementation;

import com.academy.workSearch.dao.RoleDAO;
import com.academy.workSearch.dao.implementation.UserDAOImpl;
import com.academy.workSearch.dao.implementation.UserInfoDAOImpl;
import com.academy.workSearch.dto.UserAuthDTO;
import com.academy.workSearch.dto.UserDTO;
import com.academy.workSearch.dto.UserRegistrationDTO;
import com.academy.workSearch.exceptionHandling.exceptions.NoActiveAccountException;
import com.academy.workSearch.exceptionHandling.exceptions.NoSuchEntityException;
import com.academy.workSearch.exceptionHandling.exceptions.NoUniqueEntityException;
import com.academy.workSearch.model.Role;
import com.academy.workSearch.model.User;
import com.academy.workSearch.model.UserInfo;
import com.academy.workSearch.model.enums.AccountStatus;
import com.academy.workSearch.service.JwtService;
import com.academy.workSearch.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.academy.workSearch.dto.mapper.UserAuthMapper.USER_AUTH_MAPPER;
import static com.academy.workSearch.dto.mapper.UserMapper.USER_MAPPER;
import static com.academy.workSearch.exceptionHandling.MessageConstants.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserDAOImpl userDAO;
    private final UserInfoDAOImpl userInfoDAO;
    private final RoleDAO roleDAO;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostConstruct
    private void setTypeClass() {
        userDAO.setClazz(User.class);
        userInfoDAO.setClazz(UserInfo.class);
        roleDAO.setClazz(Role.class);
    }

    @Transactional(readOnly = true)
    User getUser(String email) {
        return userDAO.getByEmail(email)
                .orElseThrow(() -> new NoSuchEntityException(NO_SUCH_ENTITY + email));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        return USER_MAPPER.map(userDAO.findAll());
    }

    @Override
    public UserAuthDTO save(UserRegistrationDTO userRegistrationDTO) {

        if (isPresentUserByEmail(userRegistrationDTO.getEmail())) {
            throw new NoUniqueEntityException(EMAIL_EXISTS + userRegistrationDTO.getEmail());
        }

        User user = USER_AUTH_MAPPER.toUser(userRegistrationDTO);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserInfoId(userInfoDAO.saveAndGetId(userInfo));
        user.setUserInfo(userInfo);
        user.setAccountStatus(AccountStatus.ACTIVE);
        Set<Role> roles = new HashSet<>();
        Role role1 = roleDAO.getByName("WORKER")
                .orElseThrow(() -> new NoSuchEntityException(NO_ROLE + "WORKER"));
        roles.add(role1);
        if (userRegistrationDTO.isEmployer()) {
            roles.add(roleDAO.getByName("EMPLOYER")
                    .orElseThrow(() -> new NoSuchEntityException(NO_ROLE + "EMPLOYER")));
        }
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        userDAO.save(user);

        UserAuthDTO userAuthDTO = new UserAuthDTO();
        userAuthDTO.setEmail(user.getEmail());
        return userAuthDTO;
    }

    @Override
    @Transactional
    public UserDTO update(UserDTO user) {
        User oldUser = userDAO.getByEmail(user.getEmail())
                .orElseThrow(() -> new NoSuchEntityException(NO_SUCH_ENTITY + user.getEmail()));
        User newUser = USER_MAPPER.toUser(user);
        newUser.setPassword(oldUser.getPassword());
        newUser.setUserId(oldUser.getUserId());
        userDAO.save(newUser);
        return USER_MAPPER.toUserDto(newUser);
    }

    @Override
    @Transactional
    public UserDTO deleteByEmail(String email) {
        User user = getUser(email);
        userDAO.deleteByEmail(email);
        return USER_MAPPER.toUserDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserAuthDTO get(UserRegistrationDTO userRegistrationDTO) {
        final User user = USER_MAPPER.toUser(getByEmail(userRegistrationDTO.getEmail()));

        if (!user.isEnabled()) {
            throw new NoActiveAccountException(NOT_ACTIVE_ACCOUNT);
        }

        List<Role> grantedAuthorities = new ArrayList<>(user.getRoles());
        grantedAuthorities.forEach(role -> role.setName("ROLE_" + role.getName()));

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userRegistrationDTO.getEmail(),
                            userRegistrationDTO.getPassword(),
                            grantedAuthorities));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(INCORRECT_USER_DATA, e);
        }
        final String accessToken = jwtService.generateAccessToken(getUser(user.getEmail()));
        final String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        UserAuthDTO userAuthDTO = new UserAuthDTO();
        userAuthDTO.setEmail(user.getEmail());
        userAuthDTO.setAccessToken(accessToken);
        userAuthDTO.setRefreshToken(refreshToken);

        return userAuthDTO;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDTO getByEmail(String email) {
        return USER_MAPPER.toUserDto(getUser(email));
    }

    @Override
    public boolean isPresentUserByEmail(String email) {
        return userDAO.getByEmail(email).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public UserAuthDTO refreshToken(UserAuthDTO userAuthDTO) {
        if (jwtService.validateRefreshToken(userAuthDTO.getRefreshToken(), userAuthDTO.getEmail())) {
            logger.info("Generating new refresh token");
            userAuthDTO.setRefreshToken(jwtService.generateRefreshToken(userAuthDTO.getEmail()));
            logger.info("Token successfully created");
            logger.info("Generating new access token");
            User user = getUser(userAuthDTO.getEmail());
            userAuthDTO.setAccessToken(jwtService.generateAccessToken(user));
            logger.info("Token successfully created");
        }
        return userAuthDTO;
    }


}
