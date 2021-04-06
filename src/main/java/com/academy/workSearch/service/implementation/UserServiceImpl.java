package com.academy.workSearch.service.implementation;

import com.academy.workSearch.dao.RoleDAO;
import com.academy.workSearch.dao.implementation.UserDAOImpl;
import com.academy.workSearch.dao.implementation.UserInfoDAOImpl;
import com.academy.workSearch.controller.UserController;
import com.academy.workSearch.controller.jwt.JwtService;
import com.academy.workSearch.dto.UserAuthDTO;
import com.academy.workSearch.dto.UserDTO;
import com.academy.workSearch.dto.UserRegistrationDTO;
import com.academy.workSearch.exceptionHandling.EntityExistsException;
import com.academy.workSearch.exceptionHandling.NoActiveAccountException;
import com.academy.workSearch.model.Role;
import com.academy.workSearch.model.User;
import com.academy.workSearch.model.UserInfo;
import com.academy.workSearch.model.enums.AccountStatus;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.academy.workSearch.dto.mapper.UserAuthMapper.USER_AUTH_MAPPER;
import static com.academy.workSearch.dto.mapper.UserMapper.USER_MAPPER;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
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

    @Override
    public List<UserDTO> findAll() {
        return USER_MAPPER.map(userDAO.findAll());
    }

    @Override
    public UserAuthDTO save(UserRegistrationDTO userRegistrationDTO) throws EntityExistsException {
        User oldUser = userDAO.getByEmail(userRegistrationDTO.getEmail());
        if (oldUser != null) {
            throw new EntityExistsException("User with email: " + userRegistrationDTO.getEmail() + "exists!");
        }

        User user = USER_AUTH_MAPPER.toUser(userRegistrationDTO);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserInfoId(userInfoDAO.saveAndGetId(userInfo));
        user.setUserInfo(userInfo);
        user.setAccountStatus(AccountStatus.ACTIVE);
        Set<Role> roles = new HashSet<>();
        Role role1 = roleDAO.getByName("WORKER");
        roles.add(role1);
        if (userRegistrationDTO.isEmployer()) {
            roles.add(roleDAO.getByName("EMPLOYER"));
        }
        user.setRoles(roles);

        user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        userDAO.save(user);

        UserAuthDTO userAuthDTO = new UserAuthDTO();
        userAuthDTO.setEmail(user.getEmail());
        return userAuthDTO;
    }

    @Override
    public UserDTO update(UserDTO user) {
        User user1 = userDAO.getByEmail(user.getEmail());
        User user2 = USER_MAPPER.toUser(user);
        user2.setPassword(user1.getPassword());
        user2.setUserId(user1.getUserId());
        userDAO.save(user2);
        return USER_MAPPER.toUserDto(user2);
    }

    @Override
    public void deleteByEmail(String email) {
        userDAO.deleteByEmail(email);
    }

    @Override
    public UserAuthDTO get(UserRegistrationDTO userRegistrationDTO) throws BadCredentialsException, NoActiveAccountException {
        final User user = USER_MAPPER.toUser(getByEmail(userRegistrationDTO.getEmail()));

        if (!user.isEnabled()) {
            throw new NoActiveAccountException("You account is not active!");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userRegistrationDTO.getEmail(),
                            userRegistrationDTO.getPassword(),
                            user.getRoles()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username or password", e);
        }
        final String jwt = jwtService.generateToken(user);

        UserAuthDTO userAuthDTO = new UserAuthDTO();
        userAuthDTO.setEmail(user.getEmail());
        userAuthDTO.setToken(jwt);

        return userAuthDTO;
    }

    public UserDTO getByEmail(String email) {
        return USER_MAPPER.toUserDto(userDAO.getByEmail(email));
    }

}
