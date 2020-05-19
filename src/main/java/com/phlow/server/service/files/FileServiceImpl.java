package com.phlow.server.service.files;

import com.phlow.server.domain.integration.yandex.YandexS3Client;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileServiceImpl implements FileService {

    private final String PHOTOS_DIRECTORY = "/photos";

    @Value("${phlow.cloud.storage.mediaMountPoint}")
    private String mediaMountPoint;

    @Autowired
    private YandexS3Client s3Client;

    @Override
    public String uploadUserPhoto(@NonNull final MultipartFile file, @NonNull final String fileKey,
                                  @NonNull final String userId) throws IOException {
        return this.s3Client.uploadFile(mediaMountPoint + PHOTOS_DIRECTORY + "/" + userId, fileKey,file);
    }

    @Override
    public void deleteUserPhoto(@NonNull final String photoId, @NonNull final String userId) {
        this.s3Client.deleteFile(mediaMountPoint + PHOTOS_DIRECTORY + "/" + userId + "/" + photoId);
    }
}
