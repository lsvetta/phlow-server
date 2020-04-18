package com.phlow.server.model.comments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.phlow.server.model.posts.PostModel;
import com.phlow.server.model.users.UserModel;
import com.phlow.server.web.View;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "comments", schema = "public", catalog = "phlow")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentModel implements Serializable {

    private static final long serialVersionUID = -8556196612513412235L;

    @Id
    @Column(name = "id")
    @Type(type = "pg-uuid")
    @GeneratedValue
    private UUID id;

    @Basic
    @Column(name = "content")
    private String content;

    @Basic
    @Column(name = "date_commented")
    @UpdateTimestamp
    private Date dateCommented;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = UserModel.class,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            })
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties(value = {"comments"})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private UserModel author;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = PostModel.class,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            })
    @JoinColumn(name = "post_id")
    @JsonIgnoreProperties(value = {"comments"})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private PostModel post;

}
