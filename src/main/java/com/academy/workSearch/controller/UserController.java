package com.academy.workSearch.controller;

import com.academy.workSearch.dto.UserAuthDTO;
import com.academy.workSearch.dto.UserDTO;
import com.academy.workSearch.dto.UserRegistrationDTO;
import com.academy.workSearch.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/v1"})
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * @return all users #only for admin
     */
    @GetMapping({"/admin/users"})
    @ApiOperation(value = "Show all users", notes = "Show information about all users ")
    public ResponseEntity<List<UserDTO>> getAll() {
        logger.info("Show all users");
        return ResponseEntity.ok(userService.findAll());
    }

    /**
     * @param userRegistrationDTO auth data
     * @return jwt tokrn
     */
    @PostMapping(value = {"/login"})
    @ApiOperation(value = "Find user by email", notes = "Find user if exists")
    public ResponseEntity<UserAuthDTO> login(@ApiParam(value = "email value for user you need to retrieve", required = true)
                                             @RequestBody UserRegistrationDTO userRegistrationDTO) {
        UserAuthDTO userAuthDTO = userService.get(userRegistrationDTO);
        return ResponseEntity.ok(userAuthDTO);

    }

    /**
     * @param user auth data
     * @return new saved user
     */
    @PostMapping({"/registration"})
    @ApiOperation(value = "Add new user", notes = "Add new user")
    public ResponseEntity<UserAuthDTO> save(@RequestBody UserRegistrationDTO user) {
        logger.info("Attempt to save user = {}", user.getEmail());
        UserAuthDTO userAuthDTO = userService.save(user);
        logger.info("Add user with email = {}", user.getEmail());
        return ResponseEntity.ok(userAuthDTO);
    }

    /**
     * @param email user
     * @return delete exoting user
     */
    @DeleteMapping({"/user/{email}"})
    @ApiOperation(value = "Delete existing user", notes = "Delete existing user")
    public ResponseEntity<?> delete(@PathVariable String email) {
        logger.info("Delete existing user {}", email);
        return ResponseEntity.ok(userService.deleteByEmail(email));
    }

    /**
     * @param email user
     * @return get user
     */
    @GetMapping({"/user/{email}"})
    @ApiOperation(value = "Get user", notes = "Get user")
    public ResponseEntity<UserDTO> get(@PathVariable String email) {
        logger.info("Find user with email = {}", email);
        return ResponseEntity.ok(userService.getByEmail(email));
    }

    /**
     * @param userAuthDTO user data
     * @return new JWT token
     */
    @PostMapping({"/refresh-token"})
    public ResponseEntity<UserAuthDTO> refreshToken(@RequestBody UserAuthDTO userAuthDTO) {
        logger.info("Update expired refresh token, from user with email = {}", userAuthDTO.getEmail());
        return ResponseEntity.ok(userService.refreshToken(userAuthDTO));
    }

    /**
     * @param token registration
     * @return if token valid activated account
     */
    @GetMapping({"/verify-email/{token}"})
    public ResponseEntity<Boolean> verifyAccount(@PathVariable String token) {
        boolean isValid = userService.isVerifyAccount(token);
        String logMessage = "Confirmation registration, check is valid token. ";
        if (isValid) {
            logMessage += "Token is valid! Account activated!";
        } else {
            logMessage += "Token is not valid! Account not activated!";
        }
        logger.info(logMessage, isValid);
        return ResponseEntity.ok(isValid);
    }
}
