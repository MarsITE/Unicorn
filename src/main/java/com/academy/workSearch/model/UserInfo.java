package com.academy.workSearch.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Past;
import java.time.LocalDate;
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

    @Column(name = "first_name", length = 20)
    @ApiModelProperty(notes = "First name of the user")
    private String firstName;

    @Column(name = "last_name", length = 20)
    @ApiModelProperty(notes = "Last name of the user")
    private String lastName;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "link_to_social_network")
    private String linkToSocialNetwork;

    @Past
    @Column(name = "date_of_birth")
    @ApiModelProperty(notes = "Date of birth of the user")
    private LocalDate dateOfBirth;

    @Column(name = "is_show_info")
    private boolean isShowInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "work_status")
    @ApiModelProperty(notes = "Work status")
    private WorkStatus workStatus;


    @Min(1)
    @Max(5)
    @Column(name = "general_rating")
    private Float generalRating;

    @Column(name = "image_url")
    @ApiModelProperty(notes = "User photo")
    private String imageUrl;

    @OneToOne
    private User user;

    @OneToMany(mappedBy = "userInfo")
    private Set<Project> projects;

    @ManyToMany
    @JoinTable(
            name = "users_info_skills",
            joinColumns = {@JoinColumn(name = "user_info_id")},
            inverseJoinColumns = {@JoinColumn(name = "skill_id")}
    )
    private Set<Skill> skills;
}
