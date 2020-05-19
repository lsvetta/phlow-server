package com.phlow.server.service.photos;

import com.phlow.server.domain.model.photos.PhotoModel;
import com.phlow.server.domain.model.photos.PhotoRepository;
import com.phlow.server.domain.model.posts.PostsRepository;
import com.phlow.server.service.files.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class PhotosServiceImpl implements PhotosService {

    private final PhotoRepository photoRepository;
    private final PostsRepository postsRepository;
    private final FileService fileService;

    @Autowired
    public PhotosServiceImpl(PhotoRepository photoRepository,
                             PostsRepository postsRepository,
                             FileService fileService) {
        this.photoRepository = photoRepository;
        this.postsRepository = postsRepository;
        this.fileService = fileService;
    }

    @Override
    public PhotoModel createPhoto(final MultipartFile photo, final String ownerId) throws IOException {
        final String photoId = UUID.randomUUID().toString();
        String url = this.fileService.uploadUserPhoto(photo, photoId, ownerId);
        return this.photoRepository.save(new PhotoModel(UUID.fromString(photoId), url, null));
    }

    @Override
    public PhotoModel updatePhoto(final MultipartFile photo, final String ownerId, final String photoId) throws IOException {
        this.fileService.uploadUserPhoto(photo, photoId, ownerId);
        return this.photoRepository.findFirstById(UUID.fromString(photoId));
    }

    @Override
    public PhotoModel getPhotoByPostId(String postId) {
        return this.postsRepository.findFirstById(UUID.fromString(postId)).getPhoto();
    }

    @Override
    @Transactional
    public void deletePhotoModel(final String photoId, final String ownerId) {
        this.fileService.deleteUserPhoto(photoId, ownerId);
        this.photoRepository.deleteFirstById(UUID.fromString(photoId));
    }
}
