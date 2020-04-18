package com.phlow.server.model.presets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.phlow.server.model.posts.PostModel;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "presets", schema = "public", catalog = "phlow")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PresetModel implements Serializable {

    private static final long serialVersionUID = -4370471231409013284L;

    @Id
    @Column(name = "id")
    @Type(type = "pg-uuid")
    @GeneratedValue
    private UUID id;

    @Basic
    @Column(name = "settings")
    private String settings;

    @OneToMany(mappedBy = "preset",
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            },
            targetEntity = PostModel.class)
    @JsonIgnoreProperties(value = {"preset"})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<PostModel> posts;
}
