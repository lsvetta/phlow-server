package com.phlow.server.domain.model.users;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.phlow.server.domain.model.followings.FollowingModel;
import com.phlow.server.domain.model.roles.RoleModel;
import com.phlow.server.web.View;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", schema = "public", catalog = "phlow")
public class UserModel implements UserDetails, Serializable {

    private static final long serialVersionUID = -7815280347748523635L;

    @Id
    @Column(name = "id")
    @Type(type = "pg-uuid")
    @JsonView(View.PUBLIC.class)
    @GeneratedValue
    private UUID id;

    @Basic
    @Column(name = "name")
    @JsonView(View.PUBLIC.class)
    private String name;

    @Basic
    @Column(name = "email")
    @JsonView(View.PUBLIC.class)
    @NonNull
    private String email;

    @Basic
    @Column(name = "username")
    @NonNull
    @JsonView(View.PUBLIC.class)
    private String username;

    @Basic
    @Column(name = "password")
    private String password;

    @ManyToMany(cascade =
            {
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            },
            fetch = FetchType.EAGER,
            targetEntity = RoleModel.class)
    @JoinTable(name = "user_roles", schema = "public",
            inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false, updatable = false,
                    referencedColumnName = "id"),
            joinColumns = @JoinColumn(name = "user_id",nullable = false, updatable = false,
                    referencedColumnName = "id"))
    @JsonIgnoreProperties(value = {"users"})
    @JsonView(View.PUBLIC.class)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<RoleModel> roles;

    @OneToMany(mappedBy = "following",
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            },
            targetEntity = FollowingModel.class)
    @JsonIgnoreProperties(value = {"following"})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<FollowingModel> followings;

    @OneToMany(mappedBy = "follower",
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            },
            targetEntity = FollowingModel.class)
    @JsonIgnoreProperties(value = {"follower"})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<FollowingModel> followers;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserModel that = (UserModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(email, that.email) &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password);
    }

    @Override
    @JsonIgnore
    public int hashCode() {
        return Objects.hash(id, name, email, username, password);
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return this.password;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(@NotNull String username) {
        this.username = username.toLowerCase();
    }

    public void setEmail(@NotNull String email) {
        this.email = email.toLowerCase();
    }
}
