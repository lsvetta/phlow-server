package com.phlow.server.domain.model.presets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.phlow.server.domain.model.posts.PostModel;
import com.phlow.server.domain.model.users.UserModel;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users_presets", schema = "public", catalog = "phlow")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPresetModel implements Serializable {
    private static final long serialVersionUID = 6644940392541574521L;

    @Id
    @Column(name = "id")
    @Type(type = "pg-uuid")
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = UserModel.class,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            })
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties(value = {"presets"})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private UserModel user;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = PresetModel.class,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            })
    @JoinColumn(name = "preset_id")
    @JsonIgnoreProperties(value = {"users"})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private PresetModel preset;

    @Basic
    @Column(name = "name")
    private String name;
}
