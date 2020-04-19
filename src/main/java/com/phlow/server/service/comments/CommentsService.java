package com.phlow.server.service.comments;

import com.phlow.server.domain.model.comments.CommentModel;

import java.util.List;

public interface CommentsService {
    public CommentModel createComment(final CommentModel commentModel);
    public void deleteComment(final String commentId);
    public List<CommentModel> getCommentsByPostId(final String postId);
}
