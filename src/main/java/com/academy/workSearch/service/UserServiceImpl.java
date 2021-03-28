package com.academy.workSearch.service;

import com.academy.workSearch.dao.UserDAOImpl;
import com.academy.workSearch.dao.UserInfoDAOImpl;
import com.academy.workSearch.dto.UserAuthDTO;
import com.academy.workSearch.dto.UserDTO;
import com.academy.workSearch.model.User;
import com.academy.workSearch.model.UserInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.validation.ValidationException;
import java.util.List;
import java.util.UUID;

import static com.academy.workSearch.dto.mapper.UserAuthMapper.USER_AUTH_MAPPER;
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

    public UserAuthDTO save(UserAuthDTO userAuthDTO) throws ValidationException {
        User user = USER_AUTH_MAPPER.toUser(userAuthDTO);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserInfoId(userInfoDAO.saveAndGetId(userInfo));
        userInfoDAO.save(userInfo);
        user.setUserInfo(userInfo);
        userDAO.save(user);
        return userAuthDTO;
    }

    @Override
    public UserDTO update(UserDTO user) throws ValidationException {
        User user1 = userDAO.getByEmail(user.getEmail());
        User user2 = USER_MAPPER.toUser(user);
        user2.setPassword(user1.getPassword());
        user2.setUserId(user1.getUserId());
        userDAO.save(user2);
        return USER_MAPPER.toUserDto(user2);
    }

    public UserDTO get(UUID id) {
        return USER_MAPPER.toUserDto(userDAO.get(id));
    }

    public void delete(UUID id) {
        userDAO.delete(id);
    }

    public void deleteByEmail(String email) {
        userDAO.deleteByEmail(email);
    }

    public UserDTO getByEmail(String email) {
        return USER_MAPPER.toUserDto(userDAO.getByEmail(email));
    }

}
