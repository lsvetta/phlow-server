package com.phlow.server.web.posts;

import com.fasterxml.jackson.annotation.JsonView;
import com.phlow.server.domain.common.ActionForbiddenException;
import com.phlow.server.domain.model.posts.PostModel;
import com.phlow.server.domain.model.posts.PostsModelMapper;
import com.phlow.server.domain.model.users.UserModel;
import com.phlow.server.domain.model.users.UserModelMapper;
import com.phlow.server.service.posts.PostsService;
import com.phlow.server.web.View;
import com.phlow.server.web.posts.dto.PostDto;
import io.swagger.annotations.Api;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.UUID;

@Api(tags = "Posts")
@RestController
@RequestMapping(
        path = "${phlow.api.prefix:/api}" + "${phlow.api.versions.v1:/v1}" + "${phlow.api.mappings.posts:/posts}",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class PostsController {

    private final PostsService postsService;
    private final PostsModelMapper postsModelMapper;
    private final UserModelMapper userModelMapper;

    @Autowired
    public PostsController(PostsService postsService,
                           PostsModelMapper postsModelMapper,
                           UserModelMapper userModelMapper) {
        this.postsService = postsService;
        this.postsModelMapper = postsModelMapper;
        this.userModelMapper = userModelMapper;
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(View.PUBLIC.class)
    public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable final String userId) {
        return ResponseEntity.ok(this.postsModelMapper.modelsToDtos(this.postsService.getPostsByAuthorId(userId)));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @JsonView(View.PUBLIC.class)
    public ResponseEntity<List<PostDto>> getPostsByFollowings(@ApiIgnore @AuthenticationPrincipal final UserModel currentUser) {
        return ResponseEntity.ok(this.postsModelMapper.modelsToDtos(this.postsService.getPostsByUserFollowings(currentUser.getId().toString())));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @JsonView(View.PUBLIC.class)
    public ResponseEntity<PostDto> createPost(@RequestBody @NonNull final PostDto postDto,
                                              @ApiIgnore @AuthenticationPrincipal final UserModel currentUser) {
        postDto.setAuthor(this.userModelMapper.modelToDto(currentUser));
        PostModel huy = this.postsModelMapper.dtoToModel(postDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.postsModelMapper.modelToDto(
                this.postsService.createPostModel(huy)));
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @JsonView(View.PUBLIC.class)
    public ResponseEntity<PostDto> updatePost(@RequestBody @NonNull final PostDto postDto,
                                              @ApiIgnore @AuthenticationPrincipal final UserModel currentUser) {
        if (this.postsService.getPostsByAuthorId(currentUser.getId().toString())
                .stream().anyMatch(postModel -> postModel.getId().equals(postDto.getId()))) {
            return ResponseEntity.ok(this.postsModelMapper.modelToDto(
                    this.postsService.updatePostModel(this.postsModelMapper.dtoToModel(postDto))));
        } else throw new ActionForbiddenException("Пост принадлежит не вам");
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @JsonView(View.PUBLIC.class)
    public ResponseEntity deletePost(@PathVariable final String postId,
                                     @ApiIgnore @AuthenticationPrincipal final UserModel currentUser) {
        if (currentUser.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN")) ||
                this.postsService.getPostsByAuthorId(currentUser.getId().toString())
                .stream().anyMatch(postModel -> postModel.getId().equals(UUID.fromString(postId)))) {
            this.postsService.deletePost(postId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
        } else throw new ActionForbiddenException("Пост принадлежит не вам");
    }
}
