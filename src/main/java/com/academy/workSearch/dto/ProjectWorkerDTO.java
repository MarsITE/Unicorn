package com.academy.workSearch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProjectWorkerDTO {
    private UUID userInfoProjectId;
    private String firstName;
    private String lastName;
    private String email;

    @JsonProperty("isApprove")
    private boolean isApprove;
}
