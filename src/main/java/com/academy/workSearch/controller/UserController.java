package com.academy.workSearch.controller;

import com.academy.workSearch.dto.UserAuthDTO;
import com.academy.workSearch.dto.UserDTO;
import com.academy.workSearch.dto.UserLoginDTO;
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
//    private final JwtProvider jwtProvider;

    @GetMapping({"/users"})
    @ApiOperation(value = "Show all users", notes = "Show information about all users in DB")
    public ResponseEntity<List<UserDTO>> showUsers() {
        this.logger.info("Show all users");
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping({"/login"})
    @ApiOperation(value = "Find user by email", notes = "Find user in DB, if user exist")
    public ResponseEntity<UserLoginDTO> getUser(@ApiParam(value = "email value for user you need to retrive", required = true)
                                                @RequestBody UserAuthDTO userAuthDTO) {
        try {
            UserLoginDTO userLogin = userService.get(userAuthDTO);
            return ResponseEntity.ok(userLogin);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping({"/registration"})
    @ApiOperation(value = "Add new user", notes = "Add new user in DB")
    public ResponseEntity<UserLoginDTO> addNewUser(@RequestBody UserAuthDTO user) {
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        try {
            userLoginDTO = userService.save(user);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        this.logger.info("Add user with email = " + user.getEmail());
        return ResponseEntity.ok(userLoginDTO);
    }

    @PutMapping({"/user-edit"})
    @ApiOperation(value = "Update existing user", notes = "Update existing user")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO user) {
        return ResponseEntity.ok(this.userService.update(user));
    }

    @DeleteMapping({"/user/{email}"})
    @ApiOperation(value = "Delete existing user", notes = "Delete existing user")
    public ResponseEntity<?> deleteUser(@PathVariable String email) {
        this.userService.deleteByEmail(email);
        return ResponseEntity.ok().build();
    }
}
