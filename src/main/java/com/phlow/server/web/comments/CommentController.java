package com.phlow.server.web.comments;

import com.fasterxml.jackson.annotation.JsonView;
import com.phlow.server.domain.common.ActionForbiddenException;
import com.phlow.server.domain.model.comments.CommentModel;
import com.phlow.server.domain.model.comments.CommentsModelMapper;
import com.phlow.server.domain.model.posts.PostModel;
import com.phlow.server.domain.model.users.UserModel;
import com.phlow.server.domain.model.users.UserModelMapper;
import com.phlow.server.service.comments.CommentsService;
import com.phlow.server.web.View;
import com.phlow.server.web.comments.dto.CommentDto;
import com.phlow.server.web.users.dto.UserDto;
import io.swagger.annotations.Api;
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

@Api(tags = "Comments")
@RestController
@RequestMapping(
        path = "${phlow.api.prefix:/api}" + "${phlow.api.versions.v1:/v1}" + "${phlow.api.mappings.comments:/comments}",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class CommentController {

    private final CommentsService commentsService;
    private final CommentsModelMapper commentsModelMapper;
    private final UserModelMapper userModelMapper;

    @Autowired
    public CommentController(CommentsService commentsService,
                             CommentsModelMapper commentsModelMapper,
                             UserModelMapper userModelMapper) {
        this.commentsService = commentsService;
        this.commentsModelMapper = commentsModelMapper;
        this.userModelMapper = userModelMapper;
    }

    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(View.PUBLIC.class)
    public ResponseEntity<List<CommentDto>> getCommentsForPost(@PathVariable final String postId) {
        return ResponseEntity.ok(this.commentsModelMapper.modelsToDtos(this.commentsService.getCommentsByPostId(postId)));
    }

    @PostMapping("/{postId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @JsonView(View.PUBLIC.class)
    public ResponseEntity<CommentDto> createComment(@PathVariable final String postId,
                                                    @RequestBody final CommentDto commentDto,
                                                    @ApiIgnore @AuthenticationPrincipal final UserModel currentUser) {
        commentDto.setAuthor(this.userModelMapper.modelToDto(currentUser));
        CommentModel commentModel = this.commentsModelMapper.dtoToModel(commentDto);
        commentModel.setPost(new PostModel());
        commentModel.getPost().setId(UUID.fromString(postId));
        return ResponseEntity.status(HttpStatus.CREATED).body(this.commentsModelMapper.modelToDto(
                this.commentsService.createComment(commentModel)));
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @JsonView(View.PUBLIC.class)
    public ResponseEntity deleteComment(@PathVariable final String commentId,
                                        @ApiIgnore @AuthenticationPrincipal final UserModel currentUser) {
        if (currentUser.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN")) ||
                this.commentsService.getCommentsByAuthorId(currentUser.getId().toString())
                        .stream().anyMatch(commentModel -> commentModel.getId().equals(UUID.fromString(commentId)))) {
            this.commentsService.deleteComment(commentId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
        } else throw new ActionForbiddenException("Комментарий принадлежит не вам");
    }
}
