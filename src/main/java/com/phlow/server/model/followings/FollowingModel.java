package com.phlow.server.model.followings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.phlow.server.model.users.UserModel;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "followings", schema = "public", catalog = "phlow")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowingModel implements Serializable {

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
    @JoinColumn(name = "following_id")
    @JsonIgnoreProperties(value = {"followings"})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private UserModel following;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = UserModel.class,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            })
    @JoinColumn(name = "follower_id")
    @JsonIgnoreProperties(value = {"followers"})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private UserModel follower;
}
