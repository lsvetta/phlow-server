package com.phlow.server.service.files;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String uploadUserPhoto(MultipartFile file, String photoId, String userId) throws IOException;
    void deleteUserPhoto(String photoId, String userId);
}
