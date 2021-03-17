package com.academy.workSearch.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
@ApiModel(description = "User Info")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    @ApiModelProperty(notes = "The unique id of the user", position = 1)
    private UUID userId;

    @Column(name = "first_name", length = 20)
    @ApiModelProperty(notes = "First name of the user", position = 2)
    private String firstName;

    @Column(name = "last_name", length = 20)
    @ApiModelProperty(notes = "Last name of the user", position = 3)
    private String lastName;

    @Email
    @Column(name = "email", length = 20)
    @ApiModelProperty(notes = "Email of the user", position = 4)
    private String email;

    @Column(name = "password")
    @ApiModelProperty(notes = "Password of the user", position = 5)
    private String password;

    @Past
    @Column(name = "date_of_birth")
    @ApiModelProperty(notes = "Date of birth of the user", position = 6)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status")
    @ApiModelProperty(notes = "Type of account", position = 7)
    private AccountStatus accountStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "work_status")
    @ApiModelProperty(notes = "Work status", position = 8)
    private WorkStatus workStatus;

    @CreationTimestamp
    @Column(name = "date_of_creation")
    @ApiModelProperty(notes = "Date of creation", position = 9)
    private LocalDateTime dateOfCreation;

    @Column(name = "image_url")
    @ApiModelProperty(notes = "User photo", position = 10)
    private String imageUrl;

    @ManyToMany
    @ApiModelProperty(notes = "User role", position = 11)
    @JoinTable(
            name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<Role> roles;

}
