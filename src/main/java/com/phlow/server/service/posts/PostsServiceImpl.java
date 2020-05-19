package com.phlow.server.service.posts;

import com.phlow.server.domain.model.followings.FollowingModel;
import com.phlow.server.domain.model.photos.PhotoModel;
import com.phlow.server.domain.model.photos.PhotoRepository;
import com.phlow.server.domain.model.posts.PostModel;
import com.phlow.server.domain.model.posts.PostsRepository;
import com.phlow.server.domain.model.presets.PresetModel;
import com.phlow.server.domain.model.presets.PresetsRepository;
import com.phlow.server.domain.model.users.UserModel;
import com.phlow.server.domain.model.users.UserRepository;
import com.phlow.server.service.followings.FollowingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostsServiceImpl implements PostsService {

    private final PostsRepository postsRepository;
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
    private final FollowingsService followingsService;
    private final PresetsRepository presetsRepository;

    public PostsServiceImpl(PostsRepository postsRepository,
                            UserRepository userRepository,
                            PhotoRepository photoRepository,
                            FollowingsService followingsService,
                            PresetsRepository presetsRepository) {
        this.postsRepository = postsRepository;
        this.userRepository = userRepository;
        this.photoRepository = photoRepository;
        this.followingsService = followingsService;
        this.presetsRepository = presetsRepository;
    }

    @Override
    public PostModel createPostModel(PostModel postModel) {
        postModel.setAuthor(this.userRepository.findUserModelById(postModel.getAuthor().getId()));
        PhotoModel photoModel;
        if(postModel.getPhoto().getId() != null) {
            photoModel = this.photoRepository.findFirstById(postModel.getPhoto().getId());
            postModel.setPhoto(photoModel);
        }
        PresetModel presetModel;
        if(postModel.getPreset().getId() != null) {
            presetModel = this.presetsRepository.findFirstById(postModel.getPreset().getId());
            postModel.setPreset(presetModel);
        }
        return this.postsRepository.save(postModel);
    }

    @Override
    public PostModel updatePostModel(PostModel post) {
        PostModel postModel = this.postsRepository.findFirstById(post.getId());
        postModel.setContent(post.getContent());
        return this.postsRepository.save(postModel);
    }

    @Override
    @Transactional
    public void deletePost(String postId) {
        this.postsRepository.deleteById(UUID.fromString(postId));
    }

    @Override
    public List<PostModel> getPostsByAuthorId(String userId) {
        return this.postsRepository.findAllByAuthorIdOrderByDateDesc(UUID.fromString(userId));
    }

    @Override
    public List<PostModel> getPostsByUserFollowings(String userId) {
        List<UUID> followingsIds = this.followingsService.getFollowings(userId).stream()
                .map(UserModel::getId)
                .collect(Collectors.toList());

        return this.postsRepository.findAllByAuthorIdIn(followingsIds);
    }
}
