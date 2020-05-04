package com.phlow.server.domain.model.posts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.phlow.server.domain.model.comments.CommentModel;
import com.phlow.server.domain.model.photos.PhotoModel;
import com.phlow.server.domain.model.presets.PresetModel;
import com.phlow.server.domain.model.users.UserModel;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "posts", schema = "public", catalog = "phlow")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostModel implements Serializable {

    private static final long serialVersionUID = 4548086231509457288L;

    @Id
    @Column(name = "id")
    @Type(type = "pg-uuid")
    @GeneratedValue
    private UUID id;

    @Basic
    @Column(name = "date")
    @UpdateTimestamp
    private Date date;

    @Basic
    @Column(name = "content")
    @NotEmpty(message = "Тело поста не может быть пустым")
    private String content;

    @OneToMany(mappedBy = "post",
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            },
            targetEntity = CommentModel.class)
    @JsonIgnoreProperties(value = {"post"})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<CommentModel> comments;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = PresetModel.class,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            })
    @JoinColumn(name = "preset_id")
    @JsonIgnoreProperties(value = {"posts"})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private PresetModel preset;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = PhotoModel.class,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            })
    @JoinColumn(name = "photo_id")
    @JsonIgnoreProperties(value = {"posts"})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NotNull(message = "Пост должен содержать фото")
    private PhotoModel photo;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = UserModel.class,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            })
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties(value = {"posts"})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private UserModel author;
}
