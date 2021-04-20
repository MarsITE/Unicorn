package com.academy.workSearch.dto;

import jdk.jfr.Timestamp;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter

public class ProjectShowInfoDTO {
    @Size(min = 1, max = 50)
    private String name;

    @Size(min = 5, max = 2000)
    private String description;

    @NotNull
    private String projectStatus;

    @Timestamp
    private String creationDate;
}
