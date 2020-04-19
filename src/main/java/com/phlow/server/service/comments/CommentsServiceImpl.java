package com.phlow.server.service.comments;

import com.phlow.server.domain.model.comments.CommentModel;
import com.phlow.server.domain.model.comments.CommentsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CommentsServiceImpl implements CommentsService {

    private final CommentsRepository commentsRepository;

    @Autowired
    public CommentsServiceImpl(CommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }

    @Override
    public CommentModel createComment(CommentModel commentModel) {
        return this.commentsRepository.save(commentModel);
    }

    @Override
    public void deleteComment(String commentId) {
        this.commentsRepository.deleteById(commentId);
    }

    @Override
    public List<CommentModel> getCommentsByPostId(String postId) {
        return this.commentsRepository.findAllByPostId(postId);
    }
}
