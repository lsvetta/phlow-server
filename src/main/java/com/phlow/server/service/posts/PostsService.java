package com.phlow.server.service.posts;

import com.phlow.server.domain.model.posts.PostModel;

import java.util.List;

public interface PostsService {
    PostModel createPostModel(final PostModel postModel);
    PostModel updatePostModel(final PostModel postModel);
    void deletePost(final String postId);
    List<PostModel> getPostsByAuthorId(final String userId);
    List<PostModel> getPostsByUserFollowings(final String userId);
}
