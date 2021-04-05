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
@AllArgsConstructor
@RequestMapping({"/api/v1/users"})
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    private final String USER_URL_PARAMETER_EMAIL = "/{email}";

    @GetMapping()
    @ApiOperation(value = "Show all users", notes = "Show information about all users")
    public ResponseEntity<List<UserDTO>> getAll() {
        this.logger.info("Show all users");
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping(USER_URL_PARAMETER_EMAIL)
    @ApiOperation(value = "Find user by email", notes = "Find user if user exist")
    public ResponseEntity<UserDTO> getUser(@ApiParam(value = "email value for user you need to retrieve", required = true) @PathVariable String email) {
        logger.info("Find user with email = {}", email);
        UserDTO user = userService.getByEmail(email).get();

        if (Objects.isNull(user)) {
            logger.error("There is no user with email = {} ", email);
        } else {
            return ResponseEntity.ok(user);
        }
       return ResponseEntity.ok(user);
    }

    @PostMapping()
    @ApiOperation(value = "Add new user", notes = "Add new user ")
    public ResponseEntity<UserDTO> add(@RequestBody UserDTO user) {
        logger.info("Add user with email = {}", user.getEmail());
        userService.save(user);
        return ResponseEntity.ok(user);
    }

    @PutMapping()
    @ApiOperation(value = "Update existing user", notes = "Update existing user")
    public ResponseEntity<UserDTO> update(@RequestBody UserDTO user) {
        logger.info("Update existing user {}", user.getEmail());
        return ResponseEntity.ok(userService.update(user));
    }

    @DeleteMapping(USER_URL_PARAMETER_EMAIL)
    @ApiOperation(value = "Delete existing user", notes = "Delete existing user")
    public ResponseEntity<UserDTO> delete(@PathVariable String email) {
        logger.info("Delete existing user {}", email);
        return ResponseEntity.ok(userService.deleteByEmail(email).get());
    }
}
