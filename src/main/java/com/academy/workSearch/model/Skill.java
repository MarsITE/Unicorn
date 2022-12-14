package com.academy.workSearch.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "skills",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = "name",
                        name = "uniqueSkillNameConstraint")
        })
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "skill_id")
    private UUID skillId;

    @NotBlank(message = "The name of skill cannot be empty")
    @Column(name = "name", length = 20, unique = true)
    private String name;

    @Column(name = "enabled")
    private boolean enabled;

}

