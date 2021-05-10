package com.academy.workSearch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkerInfoDTO {

    private String email;

    @JsonProperty("isApprove")
    private boolean isApprove;
}
