package com.academy.workSearch.dto;

import com.academy.workSearch.model.Skill;
import com.academy.workSearch.model.User;
import jdk.jfr.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ProjectDTO {

    @Id
    private UUID id;

    @Size(min = 1, max = 20)
    private String name;

    @Size(min = 5, max = 2000)
    private String description;

    @NotNull
    private String projectStatus;

    @Timestamp
    private String creationDate;

    @NotNull
    private String ownerId;

    private Set<String> skills;

}
