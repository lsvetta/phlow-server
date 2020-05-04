package com.phlow.server.domain.model.photos;

import com.phlow.server.domain.model.posts.PostModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PhotoRepository  extends CrudRepository<PhotoModel, UUID> {
    PhotoModel findFirstById(UUID id);
    PhotoModel findFirstByImageLink(String imageLink);
    void deleteAllById(UUID id);
}
