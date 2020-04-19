package com.phlow.server.web.posts;

import com.phlow.server.domain.model.posts.PostsModelMapper;
import com.phlow.server.domain.model.presets.PresetsModelMapper;
import com.phlow.server.domain.model.users.UserModel;
import com.phlow.server.service.posts.PostsService;
import com.phlow.server.service.presets.PresetsService;
import com.phlow.server.web.posts.dto.PostDto;
import com.phlow.server.web.presets.dto.PresetDto;
import io.swagger.annotations.Api;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Posts")
@RestController
@RequestMapping(
        path = "${phlow.api.prefix:/api}" + "${phlow.api.versions.v1:/v1}" + "${phlow.api.mappings.posts:/posts}",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class PostsController {

    private final PostsService postsService;
    private final PostsModelMapper postsModelMapper;

    @Autowired
    public PostsController(PostsService postsService,
                           PostsModelMapper postsModelMapper) {
        this.postsService = postsService;
        this.postsModelMapper = postsModelMapper;
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable final String userId) {
        return ResponseEntity.ok(this.postsModelMapper.modelsToDtos(this.postsService.getPostsByAuthorId(userId)));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<PostDto>> getPostsByFollowings(@AuthenticationPrincipal final UserModel currentUser) {
        return ResponseEntity.ok(this.postsModelMapper.modelsToDtos(this.postsService.getPostsByUserFollowings(currentUser.getId().toString())));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<PostDto> createPost(@RequestBody @NonNull final PostDto postDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.postsModelMapper.modelToDto(
                this.postsService.createPostModel(this.postsModelMapper.dtoToModel(postDto))));
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<PostDto> updatePost(@RequestBody @NonNull final PostDto postDto) {
        return ResponseEntity.ok(this.postsModelMapper.modelToDto(
                this.postsService.updatePostModel(this.postsModelMapper.dtoToModel(postDto))));
    }

    @DeleteMapping("/{postId")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity deletePost(@PathVariable String postId) {
        this.postsService.deletePost(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    }
}
