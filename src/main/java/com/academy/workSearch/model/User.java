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
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
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
    @Column(name = "creation_date")
    @ApiModelProperty(notes = "Creation date", position = 5)
    private LocalDateTime creationDate;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @ApiModelProperty(notes = "User role", position = 6)
    @JoinTable(
            name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<Role> roles;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @ApiModelProperty(notes = "User info", position = 7)
    @JoinColumn(name = "user_info_id")
    private UserInfo userInfo;
}
