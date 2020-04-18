package com.phlow.server.model.roles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.phlow.server.model.users.UserModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "roles", schema = "public", catalog = "phlow")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleModel implements GrantedAuthority, Comparable, Serializable {

    private static final long serialVersionUID = 8489819719753365747L;

    @Id
    @Column(name = "id")
    @JsonIgnore
    private Integer id;

    @Basic
    @Column(name = "name")
    private String name;

    @JsonIgnoreProperties(value = {"roles"})
    @ManyToMany(cascade =
            {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            },
            fetch = FetchType.EAGER,
            mappedBy = "roles",
            targetEntity = UserModel.class)
    private List<UserModel> users;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleModel that = (RoleModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String getAuthority() {
        return this.name;
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof RoleModel)) {
            throw new RuntimeException("Не могу привести к типу");
        }
        return this.getName().compareTo(((RoleModel) o).getName());
    }
}
