package com.phlow.server.domain.model.posts;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostsRepository  extends CrudRepository<PostModel, UUID> {
    void deleteById(UUID id);
    PostModel findFirstById(UUID id);
    List<PostModel> findAllByAuthorIdOrderByDateDesc(UUID userId);
    List<PostModel> findAllByAuthorIdInOrderByDateDesc(List<UUID> ids);
}
