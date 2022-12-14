package com.academy.workSearch.service.implementation;

import com.academy.workSearch.dao.UserInfoDAO;
import com.academy.workSearch.dto.UserInfoDTO;
import com.academy.workSearch.model.UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserInfoServiceImplTest {

    @InjectMocks
    private UserInfoServiceImpl userInfoService;

    @Mock
    private UserInfoDAO userInfoDAO;

    @BeforeEach
    void init() {
        userInfoDAO.setClazz(UserInfo.class);
    }

    @Test
    void update() {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUserInfoId("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f4");
        userInfoDTO.setFirstName("Diana");

        UserInfo userInfo = new UserInfo();
        userInfo.setUserInfoId(UUID.fromString("f6cea10a-2f9d-4feb-82ba-b600bb4cb5f4"));
        userInfo.setFirstName("Diana");
        when(userInfoDAO.save(any())).thenReturn(userInfo);

        assertEquals("Diana", userInfoService.update(userInfoDTO).getFirstName(), "Can't update userInfo");
    }

    @Test
    void checkIfImage() {
        Path path = Paths.get("resources/test.photo/file.txt");
        String name = "file.txt";
        String originalFileName = "file.txt";
        String contentType = "image";
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException ignored) {
        }
        MultipartFile result = new MockMultipartFile(name, originalFileName, contentType, content);

        assertFalse(userInfoService.updateImage(result, "f6cea10a-2f9d-4feb-82ba-b600bb4cb5f4", 10), "Image is updated");
    }
}
