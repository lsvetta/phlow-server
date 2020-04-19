package com.phlow.server.domain.model.posts;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostsRepository  extends CrudRepository<PostModel, UUID> {
    void deleteById(String id);
    PostModel findFirstById(String id);
    List<PostModel> findAllByAuthorIdOrderByDateDesc(String userId);
    List<PostModel> findAllByAuthorIdIn(List<String> ids);
}
