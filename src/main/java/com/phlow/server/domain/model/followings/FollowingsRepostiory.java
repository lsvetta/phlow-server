package com.phlow.server.domain.model.followings;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FollowingsRepostiory extends CrudRepository<FollowingModel, UUID> {
    FollowingModel findFirstByFollowerIdAndFollowingId(UUID followerId, UUID followingId);
}
