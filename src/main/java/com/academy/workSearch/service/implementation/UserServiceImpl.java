package com.academy.workSearch.service.implementation;

import com.academy.workSearch.dao.implementation.UserDAOImpl;
import com.academy.workSearch.dao.implementation.UserInfoDAOImpl;
import com.academy.workSearch.dto.UserDTO;
import com.academy.workSearch.model.User;
import com.academy.workSearch.model.UserInfo;
import com.academy.workSearch.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

import static com.academy.workSearch.dto.mapper.UserInfoMapper.USER_INFO_MAPPER;
import static com.academy.workSearch.dto.mapper.UserMapper.USER_MAPPER;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAOImpl userDAO;
    private final UserInfoDAOImpl userInfoDAO;

    @PostConstruct
    private void setTypeClass() {
        userDAO.setClazz(User.class);
        userInfoDAO.setClazz(UserInfo.class);
    }

    public List<UserDTO> findAll() {
        return USER_MAPPER.map(userDAO.findAll());
    }

    public UserDTO save(UserDTO user) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserInfoId(userInfoDAO.saveAndGetId(userInfo));
        userInfoDAO.save(userInfo);
        user.setUserInfo(USER_INFO_MAPPER.toUserInfoDto(userInfo));
        return USER_MAPPER.toUserDto(userDAO.save(USER_MAPPER.toUser(user)));
    }

    @Override
    public UserDTO update(UserDTO user) {
        User user1 = userDAO.getByEmail(user.getEmail()).get();
        User user2 = USER_MAPPER.toUser(user);
        user2.setPassword(user1.getPassword());
        user2.setUserId(user1.getUserId());
        userDAO.save(user2);
        return USER_MAPPER.toUserDto(user2);
    }

    public Optional<UserDTO> deleteByEmail(String email) {
        Optional<User> userDTO = userDAO.getByEmail(email);
        User user = userDTO.get();
        userDAO.deleteByEmail(email);
        UserDTO userDTO1 = USER_MAPPER.toUserDto(user);
        return Optional.of(userDTO1);
    }

    public Optional<UserDTO> getByEmail(String email) {
        Optional<User> userDTO = userDAO.getByEmail(email);
        User user = userDTO.get();
        UserDTO userDTO1 = USER_MAPPER.toUserDto(user);
        return Optional.of(userDTO1);

    }

}
