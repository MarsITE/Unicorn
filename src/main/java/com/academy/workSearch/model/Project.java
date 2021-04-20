package com.academy.workSearch.model;

import com.academy.workSearch.model.enums.ProjectStatus;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "projects", uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"name", "owner_id"})
})
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "project_id")
    private UUID projectId;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "description", length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "project_status")
    private ProjectStatus projectStatus;

    @ManyToOne(fetch = FetchType.LAZY)
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
    @JoinColumn(name = "worker_rating_id")
    private Rating workerRating;

    @OneToOne
    @JoinColumn(name = "owner_rating_id")
    private Rating employerRating;

    @CreationTimestamp
    @Column(name = "creation_date")
    private LocalDateTime creationDate;

}
