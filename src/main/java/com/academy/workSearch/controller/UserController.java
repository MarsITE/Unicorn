package com.academy.workSearch.controller;

import com.academy.workSearch.dto.UserAuthDTO;
import com.academy.workSearch.dto.UserDTO;
import com.academy.workSearch.exceptionHandling.exceptions.NoSuchEntityException;
import com.academy.workSearch.dto.UserRegistrationDTO;
import com.academy.workSearch.exceptionHandling.EntityExistsException;
import com.academy.workSearch.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping({"/api/v1"})
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping({"/admin/users"})
    @ApiOperation(value = "Show all users", notes = "Show information about all users in DB")
    public ResponseEntity<List<UserDTO>> getAll() {
        this.logger.info("Show all users");
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping(value = {"/login"})
    @ApiOperation(value = "Find user by email", notes = "Find user in DB, if user exist")
    public ResponseEntity<UserAuthDTO> login(@ApiParam(value = "email value for user you need to retrive", required = true)
                                   @RequestBody UserRegistrationDTO userRegistrationDTO) {
        UserAuthDTO userAuthDTO = userService.get(userRegistrationDTO);
        return ResponseEntity.ok(userAuthDTO);

    }

    @PostMapping({"/registration"})
    @ApiOperation(value = "Add new user", notes = "Add new user in DB")
    public ResponseEntity<UserAuthDTO> save(@RequestBody UserRegistrationDTO user) {
        UserAuthDTO userAuthDTO = new UserAuthDTO();
        try {
            userAuthDTO = userService.save(user);
        } catch (EntityExistsException e) {
            logger.error(e.getMessage());
        }
        this.logger.info("Add user with email = " + user.getEmail());
        return ResponseEntity.ok(userAuthDTO);
    }

    @PutMapping({"/user-edit"})
    @ApiOperation(value = "Update existing user", notes = "Update existing user")
    public ResponseEntity<UserDTO> update(@RequestBody UserDTO user) {
        return ResponseEntity.ok(this.userService.update(user));
    }

    @DeleteMapping({"/user/{email}"})
    @ApiOperation(value = "Delete existing user", notes = "Delete existing user")
    public ResponseEntity<UserDTO> delete(@PathVariable String email) {
        logger.info("Delete existing user {}", email);
        return ResponseEntity.ok(userService.deleteByEmail(email).orElseThrow());
    }

    @GetMapping({"/user/{email}"})
    @ApiOperation(value = "Get user", notes = "Get user")
    public ResponseEntity<UserDTO> get(@PathVariable String email) {
        UserDTO user = userService.getByEmail(email);
        logger.info("Find user with email = {}", email);
        if (user == null) {
            logger.error("There is no user with email = {} ", email);
            throw new NoSuchEntityException("There is no user with email = " + email);
        } else {
            return ResponseEntity.ok(user);
        }

    }
}
