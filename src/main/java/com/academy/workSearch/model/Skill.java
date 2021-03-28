package com.academy.workSearch.model;

import lombok.Data;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "skills")
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "skill_id")
    private UUID skillId;

    @Column(name = "name", length = 20)
    private String name;

    @Column(name = "enabled")
    private boolean enabled;
}
