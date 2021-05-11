package com.academy.workSearch.service.implementation;

import com.academy.workSearch.dao.RoleDAO;
import com.academy.workSearch.dao.UserDAO;
import com.academy.workSearch.dao.UserInfoDAO;
import com.academy.workSearch.dto.UserAuthDTO;
import com.academy.workSearch.dto.UserDTO;
import com.academy.workSearch.dto.UserRegistrationDTO;
import com.academy.workSearch.exceptionHandling.exceptions.NoActiveAccountException;
import com.academy.workSearch.exceptionHandling.exceptions.NoSuchEntityException;
import com.academy.workSearch.exceptionHandling.exceptions.NotUniqueEntityException;
import com.academy.workSearch.model.Mail;
import com.academy.workSearch.model.Role;
import com.academy.workSearch.model.User;
import com.academy.workSearch.model.UserInfo;
import com.academy.workSearch.model.enums.AccountStatus;
import com.academy.workSearch.service.EmailService;
import com.academy.workSearch.service.JwtService;
import com.academy.workSearch.service.UserService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static com.academy.workSearch.dto.mapper.UserAuthMapper.USER_AUTH_MAPPER;
import static com.academy.workSearch.dto.mapper.UserMapper.USER_MAPPER;
import static com.academy.workSearch.exceptionHandling.MessageConstants.*;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final static String TIME_TO_IMPROVE_ACCOUNT = " 1 day ";

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final FreeMarkerConfigurer freemarkerConfigurer;
    private final Environment env;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final UserDAO userDAO;
    private final UserInfoDAO userInfoDAO;
    private final RoleDAO roleDAO;


    /**
     * post construct set class type for dao
     */
    @PostConstruct
    private void setTypeClass() {
        userDAO.setClazz(User.class);
        userInfoDAO.setClazz(UserInfo.class);
        roleDAO.setClazz(Role.class);
    }

    /**
     * @param email user auth
     * @return user
     * @throws NoSuchEntityException get, if we try get user do not exists
     */
    @Transactional(readOnly = true)
    User getUser(String email) {
        return userDAO.getByEmail(email)
                .orElseThrow(() -> new NoSuchEntityException(NO_SUCH_ENTITY + email));
    }

    /**
     * @return all users
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        return USER_MAPPER.toUsersDto(userDAO.findAll());
    }

    /**
     * @param userRegistrationDTO get base data of user
     * @return email
     * method:
     * 1. check if user exists
     * 2. create user info and add to current user
     * 3. add roles
     * 4. syphred password
     * 5. generate registration token
     * 6. send message to user with activation link
     * @throws NotUniqueEntityException get, if we try get user exists
     * @throws NoSuchEntityException    get, if we try get user do not exists
     */
    @Override
    @Transactional
    public UserAuthDTO save(UserRegistrationDTO userRegistrationDTO) {
        if (isPresentUserByEmail(userRegistrationDTO.getEmail())) {
            throw new NotUniqueEntityException(EMAIL_EXISTS);
        }

        User user = USER_AUTH_MAPPER.toUser(userRegistrationDTO);
        UserInfo userInfo = new UserInfo();
        userInfo.setShowInfo(true);
        userInfo.setUserInfoId(userInfoDAO.saveAndGetId(userInfo));
        user.setUserInfo(userInfo);
        user.setAccountStatus(AccountStatus.NOT_ACTIVE);
        Set<Role> roles = new HashSet<>();
        Role worker = roleDAO.getByName("WORKER")
                .orElseThrow(() -> new NoSuchEntityException(NO_ROLE + "WORKER"));
        roles.add(worker);
        if (userRegistrationDTO.getIsEmployer()) {
            roles.add(roleDAO.getByName("EMPLOYER")
                    .orElseThrow(() -> new NoSuchEntityException(NO_ROLE + "EMPLOYER")));
        }
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        user.setRegistrationToken(jwtService.generateRegistrationToken(user.getEmail()));
        userDAO.save(user);

        sendMessageWithActivationLink(user);

        UserAuthDTO userAuthDTO = new UserAuthDTO();
        userAuthDTO.setEmail(user.getEmail());
        return userAuthDTO;
    }

    /**
     * @param user update data about user
     * @return updated user
     * @exception NoSuchEntityException get, if we try get user do not exists
     */
    @Override
    @Transactional
    public UserDTO update(UserDTO user) {
        User oldUser = getUser(user.getEmail());
        User newUser = USER_MAPPER.toUser(user);
        newUser.setPassword(oldUser.getPassword());
        newUser.setUserId(oldUser.getUserId());
        userDAO.save(newUser);
        return USER_MAPPER.toUserDto(newUser);
    }

    /**
     * @param id of possible user
     * @return user
     */
    @Transactional
    @Override
    public UserDTO delete(UUID id) {
        return USER_MAPPER.toUserDto(userDAO.delete(id));
    }

    /**
     * @param id of possible user
     * @return user if exists
     * @throws NoSuchEntityException get, if we try get user do not exists
     */
    @Transactional(readOnly = true)
    @Override
    public UserDTO get(UUID id) {
        return USER_MAPPER.toUserDto(userDAO.get(id).orElseThrow(() -> new NoSuchEntityException(NO_SUCH_USER + id)));
    }

    /**
     * @param userRegistrationDTO auth data
     * @return jwt token which include itself access and refresh tokens
     * method:
     * 1. check if user exists
     * 2. authenticate user
     * 3. if user data correct, generate tokens
     * @throws NoActiveAccountException get if user do not active  account
     * @throws BadCredentialsException  get, if we have incorrect user auth data
     */
    @Override
    @Transactional(readOnly = true)
    public UserAuthDTO get(UserRegistrationDTO userRegistrationDTO) {
        final User user = getUser(userRegistrationDTO.getEmail());

        if (!user.isEnabled() && user.getCreationDate().plusDays(1).isBefore(LocalDateTime.now()) && !user.getRegistrationToken().equals("")) {
            sendMessageWithActivationLink(user);
        }
        if (!user.isEnabled()) {
            throw new NoActiveAccountException(NOT_ACTIVE_ACCOUNT);
        }

        user.getRoles().forEach(role -> role.setName("ROLE_" + role.getName()));

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userRegistrationDTO.getEmail(),
                userRegistrationDTO.getPassword(),
                user.getRoles());
        if (!authenticationManager.authenticate(authenticationToken).isAuthenticated()) {
            throw new BadCredentialsException(INCORRECT_USER_DATA);
        }

        user.getRoles().forEach(role -> role.setName(role.getName().replace("ROLE_", "")));

        UserAuthDTO userAuthDTO = new UserAuthDTO();
        userAuthDTO.setEmail(user.getEmail());
        userAuthDTO.setAccessToken(jwtService.generateAccessToken(user));
        userAuthDTO.setRefreshToken(jwtService.generateRefreshToken(user.getEmail()));
        return userAuthDTO;
    }

    /**
     * @param email get user
     * @return check if this user exists
     */
    @Override
    public boolean isPresentUserByEmail(String email) {
        return userDAO.getByEmail(email).isPresent();
    }

    /**
     * @param userAuthDTO refresh token and email
     * @return if refresh token is valid generate new access token and refresh token
     */
    @Override
    @Transactional(readOnly = true)
    public UserAuthDTO refreshToken(UserAuthDTO userAuthDTO) {
        if (jwtService.isValidRefreshToken(userAuthDTO.getRefreshToken(), userAuthDTO.getEmail())) {
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

    /**
     * @param token registration
     * @return if registration token valid active account
     * @exception NoSuchEntityException get, if we try get user do not exists
     */
    @Transactional
    @Override
    public boolean isVerifyAccount(String token) {
        User user = userDAO.getByToken(token).orElseThrow(() -> new NoSuchEntityException(NO_SUCH_ENTITY));
        boolean isValidToken = user != null;
        if (isValidToken) {
            user.setRegistrationToken("");
            user.setAccountStatus(AccountStatus.ACTIVE);
            userDAO.save(user);
        }
        return isValidToken && jwtService.isRegistrationTokenNotExpired(token);
    }

    /**
     * method for scheduler which starts at midnight and delete all not-active accounts with
     * expired registrationToken
     */
    @Transactional
    @Override
    public void removeAllNotActiveUsersWithExpiredJWTToken() {
        List<User> users = userDAO.findNotActive();
        users.forEach(u -> {
            if (u.getCreationDate().plusDays(1).isBefore(LocalDateTime.now())) {
                userDAO.delete(u.getUserId());
            }
        });
    }

    /**
     * @param user object for whom delivery message
     *             message with confirmation link for user's account
     * @throws TemplateException get if we have bad template
     * @throws IOException       get if we have not file
     */
    @Transactional
    void sendMessageWithActivationLink(User user) {
        Mail mail = new Mail();
        mail.setSubject("Registration confirm");
        mail.setEmail(user.getEmail());
        String content = "";
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("email", user.getEmail());
            model.put("client_url", env.getProperty("CLIENT_URL"));
            if (user.getCreationDate() != null && user.getCreationDate().plusDays(1).isBefore(LocalDateTime.now())) {
                user.setRegistrationToken(jwtService.generateRegistrationToken(user.getEmail()));
                userDAO.save(user);
            }
            model.put("token", user.getRegistrationToken());
            model.put("time_to_improve", TIME_TO_IMPROVE_ACCOUNT);
            Template template = freemarkerConfigurer.getConfiguration().getTemplate("verify-email-message.txt");
            content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (IOException | TemplateException e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
        mail.setMessage(content);

        emailService.sendHtmlMessage(mail);
    }


}
