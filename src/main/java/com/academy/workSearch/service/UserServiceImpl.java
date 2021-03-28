package com.academy.workSearch.service;

import com.academy.workSearch.dao.CrudDAO;
import com.academy.workSearch.dao.UserDAOImpl;
import com.academy.workSearch.dao.UserInfoDAOImpl;
import com.academy.workSearch.dto.UserDTO;
import com.academy.workSearch.model.User;
import com.academy.workSearch.model.UserInfo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.List;
import java.util.UUID;

import static com.academy.workSearch.dto.mapper.UserInfoMapper.USER_INFO_MAPPER;
import static com.academy.workSearch.dto.mapper.UserMapper.USER_MAPPER;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {


    @Qualifier("userDAOImpl")
    @Autowired
    private final CrudDAO<User> userCrudDAO;
    private final UserDAOImpl userDAO;

    @Autowired
    private final UserInfoDAOImpl userInfoDAO;

    public List<UserDTO> findAll() {
        return USER_MAPPER.map(userCrudDAO.findAll());
    }

    public void save(UserDTO user) throws ValidationException {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserInfoId(userInfoDAO.saveAndGetId(userInfo));
        userInfoDAO.save(userInfo);
        user.setUserInfo(USER_INFO_MAPPER.toUserInfoDto(userInfo));
        userCrudDAO.save(USER_MAPPER.toUser(user));
    }

    @Override
    public UserDTO update(UserDTO user) throws ValidationException {
        User user1 = userDAO.getByEmail(user.getEmail());
        User user2 = USER_MAPPER.toUser(user);
        user2.setPassword(user1.getPassword());
        user2.setUserId(user1.getUserId());
        userCrudDAO.save(user2);
        return USER_MAPPER.toUserDto(user2);
    }

    public UserDTO get(UUID id) {
        return USER_MAPPER.toUserDto(userCrudDAO.get(id));
    }

    public void delete(UUID id) {
        userCrudDAO.delete(id);
    }

    public void deleteByEmail(String email) {
        userDAO.deleteByEmail(email);
    }

    public UserDTO getByEmail(String email) {
        return USER_MAPPER.toUserDto(userDAO.getByEmail(email));
    }

    public UUID getIdByEmail(String email) {
        return userDAO.getIdByEmail(email);
    }
}
