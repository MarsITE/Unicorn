package com.academy.workSearch.controller;

import com.academy.workSearch.dto.PhotoDTO;
import com.academy.workSearch.dto.UserInfoDTO;
import com.academy.workSearch.service.UserInfoService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/user-profile")
@AllArgsConstructor
public class UserInfoController {
    private final Logger logger = LoggerFactory.getLogger(UserInfoController.class);
    private final UserInfoService userInfoService;

    /**
     * @param user for updating
     * @return updated user
     */
    @PutMapping("/")
    @ApiOperation(value = "Update existing user info", notes = "Update existing user info")
    public ResponseEntity<UserInfoDTO> updateUserInfo(@RequestBody UserInfoDTO user) {
        logger.info("Update user-info = {} ", user.getUserInfoId());
        userInfoService.save(user);
        return ResponseEntity.ok().build();

    }

    /**
     * @param image user photo
     * @param id    user id
     * @return status updating
     */
    @PutMapping("/save-photo/{id}/{maxFileLength}")
    @ApiOperation(value = "Save user photo", notes = "Save user photo")
    public ResponseEntity<String> savePhoto(@RequestPart("image") MultipartFile image, @PathVariable(name = "id") String id, @PathVariable(name = "maxFileLength") long maxFileLength) {
        if (userInfoService.updateImage(image, id, maxFileLength)) {
            return ResponseEntity.ok().body("Photo saved!");
        } else {
            return ResponseEntity.badRequest().body("Photo not saved! It should be less than 2MB and be in format jpg, png, jpeg");
        }
    }

    /**
     * @param imageName name
     * @return photo
     */
    @GetMapping("/load-photo/{imageName}")
    @ApiOperation(value = "Load user photo", notes = "Load user photo")
    public ResponseEntity<ByteArrayResource> loadPhoto(@PathVariable(name = "imageName") String imageName) {
        PhotoDTO photoDTO = userInfoService.loadPhoto(imageName);
        return ResponseEntity
                .ok()
                .contentLength(photoDTO.getFileLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(photoDTO.getPhoto());
    }
}
