package com.academy.workSearch.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
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

    @Email
    @NotNull
    @Column(name = "email", length = 20)
    @ApiModelProperty(notes = "Email of the user", position = 2)
    private String email;

    @NotNull
    @Column(name = "password")
    @ApiModelProperty(notes = "Password of the user", position = 3)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status")
    @ApiModelProperty(notes = "Type of account", position = 4)
    private AccountStatus accountStatus;

    @CreationTimestamp
    @Column(name = "date_of_creation")
    @ApiModelProperty(notes = "Date of creation", position = 5)
    private LocalDateTime dateOfCreation;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @ApiModelProperty(notes = "User role", position = 6)
    @JoinTable(
            name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<Role> roles;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_info_id")
    private UserInfo userInfo;
}
