package com.academy.workSearch.service.implementation;

import com.academy.workSearch.controller.UserInfoController;
import com.academy.workSearch.dao.UserInfoDAO;
import com.academy.workSearch.dto.PhotoDTO;
import com.academy.workSearch.dto.SkillDetailsDTO;
import com.academy.workSearch.dto.UserInfoDTO;
import com.academy.workSearch.exceptionHandling.exceptions.NoSuchEntityException;
import com.academy.workSearch.model.Skill;
import com.academy.workSearch.model.UserInfo;
import com.academy.workSearch.service.UserInfoService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.academy.workSearch.dto.mapper.SkillDetailsMapper.SKILL_DETAILS_MAPPER;
import static com.academy.workSearch.dto.mapper.UserInfoMapper.USER_INFO_MAPPER;
import static com.academy.workSearch.exceptionHandling.MessageConstants.NO_SUCH_USER;

@Service
@AllArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {
    private final Logger logger = LoggerFactory.getLogger(UserInfoController.class);
    private final String pathToFolder = System.getProperty("user.dir") + "/photos/";

    private final UserInfoDAO userInfoDAO;

    /**
     * post construct set class type for dao
     */
    @PostConstruct
    private void setTypeClass() {
        userInfoDAO.setClazz(UserInfo.class);
    }

    /**
     * @param userInfo dto
     * @return saved user info
     */
    @Transactional
    @Override
    public UserInfoDTO update(UserInfoDTO userInfo) {
        return USER_INFO_MAPPER
                .toUserInfoDto(userInfoDAO.save(USER_INFO_MAPPER.toUserInfo(userInfo)));
    }

    /**
     * @param photo image of user
     * @param id    id for change name image
     * @return status of saving
     * @throws NoSuchEntityException get, if we try get user do not exists
     * @throws IOException           if file does not exists
     */
    @Override
    @Transactional
    public boolean updateImage(MultipartFile photo, String id, long maxFileLength) {
        boolean isImageSave = false;
        try {
            byte[] img = photo.getBytes();
            String newNameImage = id + photo.getOriginalFilename();
            Path path = Paths.get(pathToFolder + newNameImage);
            if (isFileCorrectType(new File(path.toString())) && isFileLessThanMaxFileLength(photo.getSize(), maxFileLength)) {
                Files.write(path, img);

                UUID uuid = UUID.fromString(id);
                UserInfo userInfo = userInfoDAO.get(uuid)
                        .orElseThrow(() -> new NoSuchEntityException(NO_SUCH_USER + id));
                String oldImageUrl = userInfo.getImageUrl();
                userInfo.setImageUrl(newNameImage);
                userInfoDAO.save(userInfo);

                logger.info("Image saved!");
                isImageSave = true;
                deletePreviousPhoto(oldImageUrl);
            } else {
                logger.info("Image isn't saved!");
            }
        } catch (IOException e) {
            logger.trace(Arrays.toString(Arrays.stream(e.getStackTrace()).toArray()));
        }
        return isImageSave;
    }

    /**
     * @param imageName url
     * @return photo dto
     * @exception  IOException if file does not exists
     */
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

    /**
     * @param file image
     * @return if file correct type
     */
    private boolean isFileCorrectType(File file) {
        String mimetype = new MimetypesFileTypeMap().getContentType(file);
        String type = mimetype.split("/")[0];
        return type.equals("image");
    }

    /**
     * @param fileLength    length of image
     * @param maxFileLength maxFileLength
     * @return check if fileLength =< maxFileLength
     */
    private boolean isFileLessThanMaxFileLength(long fileLength, long maxFileLength) {
        return fileLength <= maxFileLength;
    }

    @Transactional
    public List<SkillDetailsDTO> addSkills(UUID userInfoId, List<SkillDetailsDTO> skills){
        UserInfo userInfo = userInfoDAO.get(userInfoId).orElseThrow(() -> new NoSuchEntityException(NO_SUCH_USER + userInfoId));
        Set<Skill> skillsSet = userInfo.getSkills();
        skillsSet.addAll(SKILL_DETAILS_MAPPER.toSkills(skills));
        return skillsSet.stream().map(SKILL_DETAILS_MAPPER::toDto).collect(Collectors.toList());
    }

    @Transactional
    public SkillDetailsDTO deleteSkill(UUID userInfoId, SkillDetailsDTO skill) {
        UserInfo userInfo = userInfoDAO.get(userInfoId).orElseThrow(() -> new NoSuchEntityException(NO_SUCH_USER + userInfoId));
        Set<Skill> skillsSet = userInfo.getSkills();
        skillsSet.remove(SKILL_DETAILS_MAPPER.toEntity(skill));
        return skill;
    }

    private void deletePreviousPhoto(String imageUrl) {
        logger.info("Preparing to delete previous user photo");
        File file = new File(imageUrl);
        if (file.delete()) {
            logger.info("Previous photo deleted");
        } else {
            logger.info("Previous photo did not delete");
        }
    }

}
