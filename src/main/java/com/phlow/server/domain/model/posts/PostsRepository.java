package com.phlow.server.domain.model.posts;

import com.phlow.server.domain.model.presets.PresetModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostsRepository  extends CrudRepository<PostModel, UUID> {
    PostModel findFirstById(String id);
}
