package com.phlow.server.domain.integration.yandex;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@Slf4j
public class YandexS3Client implements S3Client {

    private final String HTTPS_PREFIX = "https://";

    private AmazonS3 s3client;

    @Value("${phlow.cloud.storage.endpointUrl}")
    private String endpointUrl;

    @Value("${phlow.cloud.storage.region}")
    private String region;

    @Value("${phlow.cloud.storage.bucketName}")
    private String bucketName;

    @Value("${phlow.cloud.accessKey}")
    private String accessKey;

    @Value("${phlow.cloud.secretKey}")
    private String secretKey;

    @PostConstruct
    private void initializeYandex() {
        log.info("Initializing S3 Client for Yandex Object Storage at [service endpoint: {}, region: {}]", this.endpointUrl, this.region);
        this.s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(this.accessKey, this.secretKey)))
                .withEndpointConfiguration(
                        new AmazonS3ClientBuilder.EndpointConfiguration(this.endpointUrl, this.region))
                .build();
        log.info("S3 Client for Yandex Object Storage successfully initialized");
    }

    @Override
    public String uploadFile(@NonNull final String dirPath,
                             @NonNull final String fileKey,
                           @NonNull final MultipartFile multipartFile) throws IOException {
         try {
            File file = convertMultiPartToFile(multipartFile);
            String fileUrl = dirPath + "/" + fileKey;
            uploadFileToS3bucket(fileUrl, file);
            file.delete();
            return HTTPS_PREFIX + this.endpointUrl + "/" + this.bucketName + "/" + fileUrl;
        } catch (Exception e) {
            log.error("Error while uploading file to S3 \n {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteFile(@NonNull final String path) {
        s3client.deleteObject(new DeleteObjectRequest( this.bucketName, path));
    }

    private void uploadFileToS3bucket(@NonNull String fileName, @NonNull File file) {
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    private File convertMultiPartToFile(@NonNull MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }
}
