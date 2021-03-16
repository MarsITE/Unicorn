package com.academy.workSearch.model;


import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "project_id")
    private UUID projectId;

    @Column(name = "name", length = 20)
    private String name;

    @Column(name = "description", length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "project_status")
    private ProjectStatus projectStatus;

    @OneToOne
    @JoinColumn(name = "worker_id")
    private User worker;

    @NotNull
    @OneToOne
    @JoinColumn(name = "owner_id")
    private User employer;

    @ManyToMany
    @JoinTable(
            name = "projects_skills",
            joinColumns = {@JoinColumn(name = "project_id")},
            inverseJoinColumns = {@JoinColumn(name = "skill_id")}
    )
    private Set<Skill> skills;


    @OneToOne
    @JoinColumn(name = "worker_raiting_id")
    private Rating workerRating;

    @OneToOne
    @JoinColumn(name = "owner_rating_id")
    private Rating employerRating;

    @CreationTimestamp
    @Column(name = "date_of_creation")
    private LocalDateTime dateOfCreation;

    @ManyToOne
    @JoinColumn(name = "user_info_id", insertable = false, updatable = false)
    private UserInfo userInfo;

}
