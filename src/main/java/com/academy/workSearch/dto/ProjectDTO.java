package com.academy.workSearch.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProjectDTO {
    private UUID projectId;
    private String name;
    private String description;
    private String projectStatus;
}
