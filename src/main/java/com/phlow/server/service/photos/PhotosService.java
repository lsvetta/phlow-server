package com.phlow.server.service.photos;

import com.phlow.server.domain.model.photos.PhotoModel;

public interface PhotosService {
    public PhotoModel createPhoto(final PhotoModel photoModel);
    public PhotoModel updatePhoto(final PhotoModel photoModel);
    public PhotoModel getPhotoByPostId(final String postId);
    public void deletePhotoModel(final String photoId);
}
