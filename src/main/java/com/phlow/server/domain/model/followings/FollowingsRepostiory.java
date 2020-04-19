package com.phlow.server.domain.model.followings;

import com.phlow.server.domain.model.presets.PresetModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FollowingsRepostiory extends CrudRepository<FollowingModel, UUID> {
    FollowingModel findFirstByFollowerIdAndFollowingId(String followerId, String followingId);
}
