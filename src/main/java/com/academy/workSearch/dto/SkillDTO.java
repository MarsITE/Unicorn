package com.academy.workSearch.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SkillDTO {
    private UUID skillId;
    private String name;
    private boolean enabled;
}
