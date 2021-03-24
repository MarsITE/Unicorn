package com.academy.workSearch.controller;

import com.academy.workSearch.dto.UserInfoDTO;
import com.academy.workSearch.service.UserInfoService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.naming.Binding;
import javax.validation.ValidationException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

@RestController
@RequestMapping("/api/user-info")
@AllArgsConstructor
public class UserInfoController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserInfoService userInfoService;

    @PutMapping("/")
    @ApiOperation(value = "Update existing user info", notes = "Update existing user info")
    public ResponseEntity<UserInfoDTO> updateUserInfo(@RequestBody UserInfoDTO user, BindingResult result) {
        try {
            logger.info("Update user-info = " + user.getUserInfoId());
            userInfoService.update(user);
            return ResponseEntity.ok().build();
        } catch (ValidationException e) {
            logger.trace(Arrays.toString(Arrays.stream(e.getStackTrace()).toArray()));
            logger.info("Update user-info = " + user.getUserInfoId() + " failed!");
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/saveimage")
    public ResponseEntity<MultipartFile> insertProduct(@RequestPart("image") MultipartFile image) {
        try {
            String folder = System.getProperty("user.dir") + "/photos/";
            byte[] img = image.getBytes();
            Path path = Paths.get(folder + image.getOriginalFilename());
            Files.write(path, img);
            logger.info(image.getOriginalFilename());
            logger.info(path.toString());
        } catch (IOException e) {
            logger.trace(Arrays.toString(Arrays.stream(e.getStackTrace()).toArray()));
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/loadimage/{imageUrl}")
    public ResponseEntity<?> loadProduct(@PathVariable(name = "imageUrl") String imageUrl) {
        try {
            File file = new File(System.getProperty("user.dir")+ "/photos/" + imageUrl);
            Path path = Paths.get(file.getAbsolutePath());
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

//            logger.info(inputStream.toString());
            return ResponseEntity
                    .ok()
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IOException e) {
            logger.trace(Arrays.toString(Arrays.stream(e.getStackTrace()).toArray()));
            return ResponseEntity.badRequest().build();
        }
    }
}
