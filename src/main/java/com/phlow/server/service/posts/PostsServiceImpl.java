package com.phlow.server.service.posts;

import com.phlow.server.domain.model.followings.FollowingModel;
import com.phlow.server.domain.model.posts.PostModel;
import com.phlow.server.domain.model.posts.PostsRepository;
import com.phlow.server.domain.model.users.UserModel;
import com.phlow.server.service.followings.FollowingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostsServiceImpl implements PostsService {

    private final PostsRepository postsRepository;
    private final FollowingsService followingsService;

    public PostsServiceImpl(PostsRepository postsRepository,
                            FollowingsService followingsService) {
        this.postsRepository = postsRepository;
        this.followingsService = followingsService;
    }

    @Override
    public PostModel createPostModel(PostModel postModel) {
        return this.postsRepository.save(postModel);
    }

    @Override
    public PostModel updatePostModel(PostModel postModel) {
        return this.postsRepository.save(postModel);
    }

    @Override
    public void deletePost(String postId) {
        this.postsRepository.deleteById(postId);
    }

    @Override
    public List<PostModel> getPostsByAuthorId(String userId) {
        return this.postsRepository.findAllByAuthorIdOrderByDateDesc(userId);
    }

    @Override
    public List<PostModel> getPostsByUserFollowings(String userId) {
        List<String> followingsIds = this.followingsService.getFollowings(userId).stream()
                .map(UserModel::getId)
                .map(UUID::toString)
                .collect(Collectors.toList());

        return this.postsRepository.findAllByAuthorIdIn(followingsIds);
    }
}
