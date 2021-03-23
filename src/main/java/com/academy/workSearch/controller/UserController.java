package com.academy.workSearch.controller;

import com.academy.workSearch.dto.UserDTO;
import com.academy.workSearch.exceptionHandling.NoSuchUserException;
import com.academy.workSearch.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @GetMapping("/users")
    @ApiOperation(value = "Show all users", notes = "Show information about all users in DB")
    public List<UserDTO> showUsers() {
        logger.info("Show all users");
        return userService.findAll();
    }

    @GetMapping("/user/{email}")
    @ApiOperation(value = "Find user by email", notes = "Find user in DB, if user exist")
    public UserDTO getUser(@ApiParam(value = "email value for user you need to retrive", required = true)
                           @PathVariable String email) {
        UserDTO user = userService.getByEmail(email);
        logger.info("Find user with email = " + email);
        if (user == null) {
            logger.error("There is no user with email = " + email + " in Database");
            throw new NoSuchUserException("There is no user with email = " + email + " in Database");
        }
        return user;
    }

    @PostMapping("/user")
    @ApiOperation(value = "Add new user", notes = "Add new user in DB")
    public UserDTO addNewUser(@RequestBody UserDTO user) {
        try {
            userService.save(user);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        logger.info("Add user with email = " + user.getEmail());
        return user;
    }

    @PutMapping("/user")
    @ApiOperation(value = "Update existing user", notes = "Update existing user")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO user) {
        try {
            return ResponseEntity.ok(userService.update(user));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        logger.info("Update user with email = " + user.getEmail());
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/user/{email}")
    @ApiOperation(value = "Delete existing user", notes = "Delete existing user")
    public void deleteUser(@PathVariable String email) {
        UserDTO user = userService.getByEmail(email);
        if (user == null) {
            logger.error("There is no user with email = " + email + " in Database");
            throw new NoSuchUserException("There is no user with email = " + email + " in Database");
        }
        UUID id = userService.getIdByEmail(email);
        userService.delete(id);
        logger.info("Delete user with ID = " + id);
    }
}
