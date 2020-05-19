package com.phlow.server.service.comments;

import com.phlow.server.domain.model.comments.CommentModel;
import com.phlow.server.domain.model.comments.CommentsRepository;
import com.phlow.server.domain.model.posts.PostsRepository;
import com.phlow.server.domain.model.users.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class CommentsServiceImpl implements CommentsService {

    private final CommentsRepository commentsRepository;
    private final UserRepository userRepository;
    private final PostsRepository postsRepository;

    @Autowired
    public CommentsServiceImpl(CommentsRepository commentsRepository,
                               UserRepository userRepository,
                               PostsRepository postsRepository) {
        this.commentsRepository = commentsRepository;
        this.userRepository = userRepository;
        this.postsRepository = postsRepository;
    }

    @Override
    public CommentModel createComment(CommentModel commentModel) {
        commentModel.setAuthor(this.userRepository.findUserModelById(commentModel.getAuthor().getId()));
        commentModel.setPost(this.postsRepository.findFirstById(commentModel.getPost().getId()));
        return this.commentsRepository.save(commentModel);
    }

    @Override
    @Transactional
    public void deleteComment(String commentId) {
        this.commentsRepository.deleteById(UUID.fromString(commentId));
    }

    @Override
    public List<CommentModel> getCommentsByPostId(String postId) {
        return this.commentsRepository.findAllByPostIdOrderByDateCommentedDesc(UUID.fromString(postId));
    }

    @Override
    public List<CommentModel> getCommentsByAuthorId(String userId) {
        return this.commentsRepository.findAllByAuthorId(UUID.fromString(userId));
    }
}
