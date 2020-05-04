package com.phlow.server.service.followings;

import com.phlow.server.domain.model.users.UserModel;

import java.util.List;

public interface FollowingsService {

    public List<UserModel> getFollowers(final String userId);
    public List<UserModel> getFollowings(final String userId);
    public void follow(final String followingUserId, final String followerUserId);
    public void unfollow(final String followingUserId, final String followerUserId);
}
