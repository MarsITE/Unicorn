package com.academy.workSearch.model;

import com.academy.workSearch.model.enums.AccountStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import static com.academy.workSearch.model.enums.AccountStatus.ACTIVE;

@Data
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
@ApiModel(description = "User Info")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    @ApiModelProperty(notes = "The unique id of the user", position = 1)
    private UUID userId;

    @Email
    @NotNull
    @Column(name = "email", length = 50)
    @ApiModelProperty(notes = "Email of the user", position = 2)
    private String email;

    @NotNull
    @Column(name = "password")
    @ApiModelProperty(notes = "Password of the user", position = 3)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", columnDefinition = "AccountStatus default NOT_ACTIVE")
    @ApiModelProperty(notes = "Type of account", position = 4)
    private AccountStatus accountStatus;

    @CreationTimestamp
    @Column(name = "creation_date")
    @ApiModelProperty(notes = "Creation date", position = 5)
    private LocalDateTime creationDate;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @ApiModelProperty(notes = "User role", position = 6)
    @JoinTable(
            name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<Role> roles;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @ApiModelProperty(notes = "User info", position = 7)
    @JoinColumn(name = "user_info_id")
    private UserInfo userInfo;

    @Column(name = "token")
    private String token;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountStatus == ACTIVE;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return accountStatus == ACTIVE;
    }
}
