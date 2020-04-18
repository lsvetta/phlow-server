package com.phlow.server.model.photos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.phlow.server.model.comments.CommentModel;
import com.phlow.server.model.posts.PostModel;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "photos", schema = "public", catalog = "phlow")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoModel implements Serializable {

    private static final long serialVersionUID = -1178588825021714761L;

    @Id
    @Column(name = "id")
    @Type(type = "pg-uuid")
    @GeneratedValue
    private UUID id;

    @Basic
    @Column(name = "image_link")
    private String imageLink;

    @OneToMany(mappedBy = "photo",
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            },
            targetEntity = PostModel.class)
    @JsonIgnoreProperties(value = {"photo"})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<PostModel> posts;
}
