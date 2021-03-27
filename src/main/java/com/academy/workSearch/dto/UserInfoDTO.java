package com.academy.workSearch.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserInfoDTO {
    private String userInfoId;
    private String firstName;
    private String lastName;
    private String phone;
    private String linkToSocialNetwork;
    private String dateOfBirth;
    private boolean isShowInfo;
    private String workStatus;
    private String imageUrl;
    private String generalRating;
}
