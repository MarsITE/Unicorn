package com.academy.workSearch.service.implementation;

import com.academy.workSearch.controller.UserInfoController;
import com.academy.workSearch.dao.UserInfoDAO;
import com.academy.workSearch.dto.PhotoDTO;
import com.academy.workSearch.dto.UserInfoDTO;
import com.academy.workSearch.exceptionHandling.exceptions.NoSuchEntityException;
import com.academy.workSearch.model.User;
import com.academy.workSearch.model.UserInfo;
import com.academy.workSearch.service.UserInfoService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

import static com.academy.workSearch.dto.mapper.UserInfoMapper.USER_INFO_MAPPER;
import static com.academy.workSearch.exceptionHandling.MessageConstants.NO_SUCH_ENTITY;

@Service
@Transactional
@AllArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {
    private final Logger logger = LoggerFactory.getLogger(UserInfoController.class);
    private final String pathToFolder = System.getProperty("user.dir") + "/photos/";

    private final UserInfoDAO userInfoDAO;

    @PostConstruct
    private void setTypeClass() {
        userInfoDAO.setClazz(UserInfo.class);
    }

    public UserInfoDTO save(UserInfoDTO userInfo) {
        UserInfo userInfo1 = USER_INFO_MAPPER.toUserInfo(userInfo);
        userInfoDAO.save(userInfo1);
        return userInfo;

    }

    @Override
    public boolean updateImage(MultipartFile photo, String id) {
        boolean isImageSave = false;
        try {
            byte[] img = photo.getBytes();
            String newNameImage = id + photo.getOriginalFilename();
            Path path = Paths.get(pathToFolder + newNameImage);
            Files.write(path, img);
            logger.info(photo.getOriginalFilename());
            logger.info(path.toString());

            UUID uuid = UUID.fromString(id);
            UserInfo userInfo = userInfoDAO.get(uuid).orElseThrow(()->new NoSuchEntityException(NO_SUCH_ENTITY + id));
            userInfo.setImageUrl(newNameImage);
            userInfoDAO.save(userInfo);

            isImageSave = true;
        } catch (IOException e) {
            logger.trace(Arrays.toString(Arrays.stream(e.getStackTrace()).toArray()));
        }
        return isImageSave;
    }

    @Override
    public PhotoDTO loadPhoto(String imageName) {
        PhotoDTO photoDTO = new PhotoDTO();
        try {
            File file = new File(pathToFolder + imageName);
            photoDTO.setFileLength(file.length());

            Path path = Paths.get(file.getAbsolutePath());
            photoDTO.setPhoto(new ByteArrayResource(Files.readAllBytes(path)));
            return photoDTO;
        } catch (IOException e) {
            logger.trace(Arrays.toString(Arrays.stream(e.getStackTrace()).toArray()));
        }
        return photoDTO;
    }

}
