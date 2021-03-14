package com.academy.workSearch.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "users_info")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_info_id")
    private UUID userInfoId;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "link_to_social_network")
    private String linkToSocialNetwork;

    @Column(name = "is_show_info")
    private boolean isShowInfo;

    @Min(1)
    @Max(5)
    @Column(name = "general_rating")
    private Float generalRating;

    @OneToOne
    private User user;

    @OneToMany(mappedBy = "userInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Project> projects;

    @ManyToMany
    @JoinTable(
            name = "users_info_skills",
            joinColumns = {@JoinColumn(name = "user_info_id")},
            inverseJoinColumns = {@JoinColumn(name = "skill_id")}
    )
    private Set<Skill> skills;
}
