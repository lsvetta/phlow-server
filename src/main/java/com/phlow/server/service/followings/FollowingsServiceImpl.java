package com.phlow.server.service.followings;

import com.phlow.server.domain.model.followings.FollowingModel;
import com.phlow.server.domain.model.followings.FollowingsRepostiory;
import com.phlow.server.domain.model.presets.PresetModel;
import com.phlow.server.domain.model.users.UserModel;
import com.phlow.server.domain.model.users.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FollowingsServiceImpl implements FollowingsService {

    private final UserRepository userRepository;
    private final FollowingsRepostiory followingsRepostiory;

    @Autowired
    public FollowingsServiceImpl(UserRepository userRepository,
                                 FollowingsRepostiory followingsRepostiory) {
        this.userRepository = userRepository;
        this.followingsRepostiory = followingsRepostiory;
    }

    @Override
    public List<UserModel> getFollowers(String userId) {
        return this.userRepository.findUserModelById(UUID.fromString(userId))
                .getFollowers().stream().map(FollowingModel::getFollower).distinct().collect(Collectors.toList());
    }

    @Override
    public List<UserModel> getFollowings(String userId) {
        return this.userRepository.findUserModelById(UUID.fromString(userId)).getFollowings().stream()
                .map(FollowingModel::getFollowing).distinct().collect(Collectors.toList());
    }

    @Override
    public void follow(String followingUserId, String followerUserId) {
        FollowingModel followingModel = this.followingsRepostiory
                .findFirstByFollowerIdAndFollowingId(UUID.fromString(followerUserId), UUID.fromString(followingUserId));
        if(followingModel == null) {
            followingModel = new FollowingModel(null,
                    this.userRepository.findUserModelById(UUID.fromString(followingUserId)),
                    this.userRepository.findUserModelById(UUID.fromString(followerUserId)));
            this.followingsRepostiory.save(followingModel);
        }
    }

    @Override
    @Transactional
    public void unfollow(String followingUserId, String followerUserId) {
        FollowingModel followingModel = this.followingsRepostiory.findFirstByFollowerIdAndFollowingId(
                UUID.fromString(followerUserId), UUID.fromString(followingUserId));
        if(followingModel != null) {
            this.followingsRepostiory.delete(followingModel);
        }
    }
}
