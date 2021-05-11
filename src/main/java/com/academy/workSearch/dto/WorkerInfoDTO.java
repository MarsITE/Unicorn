package com.academy.workSearch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkerInfoDTO {
//    private String firstName;
//    private String lastName;
    private String email;
//    private UserInfoDTO userInfoDTO;

    @JsonProperty("isApprove")
    private boolean isApprove;
}
