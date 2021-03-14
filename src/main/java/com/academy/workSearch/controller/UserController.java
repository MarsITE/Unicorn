package com.academy.workSearch.controller;

import com.academy.workSearch.exceptionHandling.NoSuchUserException;
import com.academy.workSearch.model.User;
import com.academy.workSearch.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private final UserService userService;

    @GetMapping("/users")
    @ApiOperation(value = "Show all users", notes = "Show information about all users in DB")
    public List<User> showUsers() {
        logger.info("Show all users");
        return userService.findAll();
    }

    @GetMapping("/user/{id}")
    @ApiOperation(value = "Find user by ID", notes = "Find user in DB, if user exist")
    public User getUser(@ApiParam(value = "ID value for user you need to retrive", required = true)
                            @PathVariable UUID id) {
        User user = userService.get(id);
        logger.info("Find user with ID = " + id);
        if (user == null) {
            logger.error("There is no user with ID = " + id + " in Database");
            throw new NoSuchUserException("There is no user with ID = " + id + " in Database");
        }
        return user;
    }

    @PostMapping("/user")
    public User addNewUser(@RequestBody User user) {
        userService.save(user);
        logger.info("Add user with ID = " + user.getUserId());
        return user;
    }

    @PutMapping("/user")
    public User updateUser(@RequestBody User user) {
        userService.save(user);
        logger.info("Update user with ID = " + user.getUserId());
        return user;
    }

    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable UUID id) {
        User user = userService.get(id);
        if (user == null) {
            logger.error("There is no user with ID = " + id + " in Database");
            throw new NoSuchUserException("There is no user with ID = " + id + " in Database");
        }
        userService.delete(id);
        logger.info("Delete user with ID = " + user.getUserId());
    }
}
