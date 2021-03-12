package com.academy.workSearch.model;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
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
    private User worker;

    @OneToOne
    private User employer;

    @ManyToMany
    @JoinTable(
            name = "projects_skills",
            joinColumns = {@JoinColumn(name = "project_id")},
            inverseJoinColumns = {@JoinColumn(name = "skill_id")}
    )
    private Set<Skill> skills;


    @OneToOne
    private Rating workerRating;

    @OneToOne
    private Rating employerRating;

    @Column(name = "date_of_creation")
    private LocalDate dateOfCreation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", referencedColumnName = "user_info_id", insertable = false, updatable = false)
    private UserInfo userInfo;

    public Project() {
        dateOfCreation = LocalDate.now();
    }
}
