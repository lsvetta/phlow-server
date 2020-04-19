package com.phlow.server.web.comments;

import com.phlow.server.domain.model.comments.CommentsModelMapper;
import com.phlow.server.service.comments.CommentsService;
import com.phlow.server.web.comments.dto.CommentDto;
import com.phlow.server.web.users.dto.UserDto;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Comments")
@RestController
@RequestMapping(
        path = "${phlow.api.prefix:/api}" + "${phlow.api.versions.v1:/v1}" + "${phlow.api.mappings.comments:/comments}",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class CommentController {

    private final CommentsService commentsService;
    private final CommentsModelMapper commentsModelMapper;

    @Autowired
    public CommentController(CommentsService commentsService,
                             CommentsModelMapper commentsModelMapper) {
        this.commentsService = commentsService;
        this.commentsModelMapper = commentsModelMapper;
    }

    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<CommentDto>> getCommentsForPost(@PathVariable final String postId) {
        return ResponseEntity.ok(this.commentsModelMapper.modelsToDtos(this.commentsService.getCommentsByPostId(postId)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<CommentDto> createComment(@RequestBody final CommentDto commentDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.commentsModelMapper.modelToDto(this.commentsService.createComment(
                this.commentsModelMapper.dtoToModel(commentDto))));
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity deleteComment(@PathVariable final String commentId) {
        this.commentsService.deleteComment(commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    }
}
