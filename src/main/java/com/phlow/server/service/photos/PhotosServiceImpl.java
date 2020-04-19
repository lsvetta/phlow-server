package com.phlow.server.service.photos;

import com.phlow.server.domain.model.photos.PhotoModel;
import com.phlow.server.domain.model.photos.PhotoRepository;
import com.phlow.server.domain.model.posts.PostsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PhotosServiceImpl implements PhotosService {

    private final PhotoRepository photoRepository;
    private final PostsRepository postsRepository;

    public PhotosServiceImpl(PhotoRepository photoRepository,
                             PostsRepository postsRepository) {
        this.photoRepository = photoRepository;
        this.postsRepository = postsRepository;
    }

    @Override
    public PhotoModel createPhoto(PhotoModel photoModel) {
        return this.photoRepository.save(photoModel);
    }

    @Override
    public PhotoModel updatePhoto(PhotoModel photoModel) {
        return this.photoRepository.save(photoModel);
    }

    @Override
    public PhotoModel getPhotoByPostId(String postId) {
        return this.postsRepository.findFirstById(postId).getPhoto();
    }

    @Override
    public void deletePhotoModel(String photoId) {
        this.photoRepository.deleteAllById(photoId);
    }
}
