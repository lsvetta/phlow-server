package com.phlow.server.domain.model.comments;

import com.phlow.server.domain.model.photos.PhotoModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentsRepository extends CrudRepository<CommentModel, UUID> {
    void deleteById(UUID id);
    List<CommentModel> findAllByPostIdOrderByDateCommentedDesc(UUID id);
    List<CommentModel> findAllByAuthorId(UUID id);
}
