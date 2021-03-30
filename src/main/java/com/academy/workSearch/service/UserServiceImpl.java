package com.academy.workSearch.service;

import com.academy.workSearch.dao.RoleDAOImpl;
import com.academy.workSearch.dao.UserDAO;
import com.academy.workSearch.dao.UserDAOImpl;
import com.academy.workSearch.dao.UserInfoDAOImpl;
import com.academy.workSearch.dto.UserAuthDTO;
import com.academy.workSearch.dto.UserDTO;
import com.academy.workSearch.model.AccountStatus;
import com.academy.workSearch.model.Role;
import com.academy.workSearch.model.User;
import com.academy.workSearch.model.UserInfo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;

import static com.academy.workSearch.dto.mapper.UserAuthMapper.USER_AUTH_MAPPER;
import static com.academy.workSearch.dto.mapper.UserMapper.USER_MAPPER;

@Service(value = "UserServiceImpl")
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {
    private final UserDAOImpl userDAO;
    private final UserInfoDAOImpl userInfoDAO;
    private final RoleDAOImpl roleDAO;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    private void setTypeClass() {
        userDAO.setClazz(User.class);
        userInfoDAO.setClazz(UserInfo.class);
        roleDAO.setClazz(Role.class);
    }

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException, BadCredentialsException {
        User user = userDAO.getByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;//todo ask
    }

    @Override
    public List<UserDTO> findAll() {
        return USER_MAPPER.map(userDAO.findAll());
    }

    @Override
    public UserAuthDTO save(UserAuthDTO userAuthDTO) {
        User user = USER_AUTH_MAPPER.toUser(userAuthDTO);
        UserInfo userInfo = new UserInfo();//todo ask
        userInfo.setUserInfoId(userInfoDAO.saveAndGetId(userInfo));
        userInfoDAO.save(userInfo);
        user.setUserInfo(userInfo);
        user.setAccountStatus(AccountStatus.ACTIVE);
        Role role1 = roleDAO.getByName("WORKER");
        Role role2 = new Role();
        if (userAuthDTO.isEmployer()) {
            role2 = roleDAO.getByName("EMPLOYER");
        }
        Set<Role> roles = new HashSet<>(Arrays.asList(role1, role2));
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(userAuthDTO.getPassword()));
        userDAO.save(user);
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
    public UserAuthDTO get(String email, String password) throws BadCredentialsException {
        UserAuthDTO user = USER_AUTH_MAPPER.toUserAuthDto(loadUserByUsername(email));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Incorrect login or password");
        }

        return user;
    }

}
