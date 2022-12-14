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
import java.util.UUID;

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
     * @return jwt token
     */
    @PostMapping(value = {"/login"})
    @ApiOperation(value = "Auth", notes = "Find user if exists")
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
     * @param id user
     * @return delete exiting user
     */
    @DeleteMapping({"/user/{id}"})
    @ApiOperation(value = "Delete existing user", notes = "Delete existing user")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        logger.info("Delete existing user {}", id);
        return ResponseEntity.ok(userService.delete(id));
    }

    /**
     * @param id user
     * @return get user
     * get user by id
     */
    @GetMapping({"/user/{id}"})
    @ApiOperation(value = "Get user", notes = "Get user")
    public ResponseEntity<UserDTO> get(@PathVariable UUID id) {
        logger.info("Find user with id = {}", id);
        return ResponseEntity.ok(userService.get(id));
    }

    /**
     * @param userAuthDTO user data
     * @return new JWT token
     */
    @PostMapping({"/refresh-token"})
    public ResponseEntity<UserAuthDTO> refreshToken(@RequestBody UserAuthDTO userAuthDTO) {
        logger.info("Update expired refresh token, from user with email = {}", userAuthDTO.getEmail());
        UserAuthDTO authDTO = userService.refreshToken(userAuthDTO);
        return ResponseEntity.ok(authDTO);
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
