package com.academy.workSearch.controller;

import com.academy.workSearch.dto.UserAuthDTO;
import com.academy.workSearch.dto.UserDTO;
import com.academy.workSearch.dto.UserRegistrationDTO;
import com.academy.workSearch.exceptionHandling.exceptions.NoUniqueEntityException;
import com.academy.workSearch.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping({"/api/v1"})
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping({"/admin/users"})
    @ApiOperation(value = "Show all users", notes = "Show information about all users ")
    public ResponseEntity<List<UserDTO>> getAll() {
        logger.info("Show all users");
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping(value = {"/login"})
    @ApiOperation(value = "Find user by email", notes = "Find user if exists")
    public ResponseEntity<UserAuthDTO> login(@ApiParam(value = "email value for user you need to retrieve", required = true)
                                             @RequestBody UserRegistrationDTO userRegistrationDTO) {
        UserAuthDTO userAuthDTO = userService.get(userRegistrationDTO);
        return ResponseEntity.ok(userAuthDTO);

    }

    @PostMapping({"/registration"})
    @ApiOperation(value = "Add new user", notes = "Add new user")
    public ResponseEntity<UserAuthDTO> save(@RequestBody UserRegistrationDTO user) {
        logger.info("Attempt to save user = {}", user.getEmail());
        UserAuthDTO userAuthDTO = userService.save(user);
        logger.info("Add user with email = {}", user.getEmail());
        return ResponseEntity.ok(userAuthDTO);
    }

    @PutMapping({"/user"})
    @ApiOperation(value = "Update existing user", notes = "Update existing user")
    public ResponseEntity<UserDTO> update(@RequestBody UserDTO user) {
        return ResponseEntity.ok(userService.update(user));
    }

    @DeleteMapping({"/user/{email}"})
    @ApiOperation(value = "Delete existing user", notes = "Delete existing user")
    public ResponseEntity<?> delete(@PathVariable String email) {
        logger.info("Delete existing user {}", email);
        return ResponseEntity.ok(userService.deleteByEmail(email));
    }

    @GetMapping({"/user/{email}"})
    @ApiOperation(value = "Get user", notes = "Get user")
    public ResponseEntity<UserDTO> get(@PathVariable String email) {
        logger.info("Find user with email = {}", email);
        return ResponseEntity.ok(userService.getByEmail(email));
    }

    @PostMapping({"/refresh-token"})
    public ResponseEntity<UserAuthDTO> refreshToken(@RequestBody UserAuthDTO userAuthDTO) {
        logger.info("Update expired refresh token, from user with email = {}", userAuthDTO.getEmail());
        return ResponseEntity.ok(userService.refreshToken(userAuthDTO));
    }
}
