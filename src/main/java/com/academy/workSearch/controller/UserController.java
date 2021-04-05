package com.academy.workSearch.controller;

import com.academy.workSearch.dto.UserDTO;
import com.academy.workSearch.exceptionHandling.exceptions.NoSuchEntityException;
import com.academy.workSearch.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping({"/api/v1"})
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @GetMapping({"/users"})
    @ApiOperation(value = "Show all users", notes = "Show information about all users ")
    public ResponseEntity<List<UserDTO>> showUsers() {
        logger.info("Show all users");
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping({"/user/{email}"})
    @ApiOperation(value = "Find user by email", notes = "Find user if exists")
    public ResponseEntity<UserDTO> getUser(@ApiParam(value = "email value for user you need to retrieve", required = true) @PathVariable String email) {
        UserDTO user = userService.getByEmail(email);
        logger.info("Find user with email = " + email);
        if (user == null) {
           logger.error("There is no user with email = {} ", email );
            throw new NoSuchEntityException("There is no user with email = " + email );
        } else {
            return ResponseEntity.ok(user);
        }
    }

    @PostMapping({"/user"})
    @ApiOperation(value = "Add new user", notes = "Add new user ")
    public ResponseEntity<UserDTO> addNewUser(@RequestBody UserDTO user) {
        userService.save(user);
        logger.info("Add user with email = " + user.getEmail());
        return ResponseEntity.ok(user);
    }

    @PutMapping({"/user"})
    @ApiOperation(value = "Update existing user", notes = "Update existing user")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO user) {
        return ResponseEntity.ok(userService.update(user));
    }

    @DeleteMapping({"/user/{email}"})
    @ApiOperation(value = "Delete existing user", notes = "Delete existing user")
    public ResponseEntity<?> deleteUser(@PathVariable String email) {
        userService.deleteByEmail(email);
        return ResponseEntity.ok().build();
    }
}
