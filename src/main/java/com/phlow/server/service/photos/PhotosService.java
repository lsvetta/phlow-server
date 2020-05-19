package com.phlow.server.service.photos;

import com.phlow.server.domain.model.photos.PhotoModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PhotosService {
    public PhotoModel createPhoto(final MultipartFile photo, final String ownerId) throws IOException;
    public PhotoModel updatePhoto(final MultipartFile photo, final String ownerId, final String photoId) throws IOException;
    public PhotoModel getPhotoByPostId(final String postId);
    public void deletePhotoModel(final String photoId, final String ownerId);
}
