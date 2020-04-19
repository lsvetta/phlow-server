package com.phlow.server.service.followings;

import com.phlow.server.domain.model.presets.PresetModel;
import com.phlow.server.domain.model.users.UserModel;

import java.util.List;
import java.util.UUID;

public interface FollowingsService {

    public List<UserModel> getFollowers(final String userId);
    public List<UserModel> getFollowings(final String userId);
    public void follow(final String followingUserId, final String followerUserId);
    public void unfollow(final String followingUserId, final String followerUserId);
}
