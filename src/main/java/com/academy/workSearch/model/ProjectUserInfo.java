package com.academy.workSearch.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "users_info_projects")
public class ProjectUserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "users_info_projects_id")
    private UUID userInfoProjectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_info_id")
    private UserInfo userInfo;

    @Column(name = "is_approve")
    private boolean isApprove;

    @OneToOne
    @JoinColumn(name = "worker_rating_id")
    private Rating workerRating;
}
