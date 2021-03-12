package com.academy.workSearch.controller;

import com.academy.workSearch.exceptionHandling.NoSuchUserException;
import com.academy.workSearch.model.User;
import com.academy.workSearch.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> showUsers() {
        logger.info("Show all users");
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable UUID id) {
        User user = userService.getUser(id);
        if (user == null) {
            throw new NoSuchUserException("There is no user with ID = " + id + " in Database");
        }
        return user;
    }

    @PostMapping("/users")
    public User addNewUser(@RequestBody User user){
        userService.saveUser(user);
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user){
        userService.saveUser(user);
        return user;
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable UUID id){
        User user = userService.getUser(id);
        if (user == null) {
            throw new NoSuchUserException("There is no user with ID = " + id + " in Database");
        }
        userService.deleteUser(id);
        return "User with ID = " + id + " was deleted";
    }
}
