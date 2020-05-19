package com.phlow.server.domain.integration.yandex;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

interface S3Client {

    /**
     * Общий метод для загрузки файлов в S3. Если файл по указанному ключу существует, он будет перезаписан
     * @param dirPath - путь до папки внутри бакета по которому загрузить файл
     * @param fileKey - имя файла
     * @param multipartFile - сам файл
     * @return - URL-ссылку на загруженынй файл
     */
    String uploadFile(final String dirPath, final String fileKey, MultipartFile multipartFile) throws IOException;

    /**
     * Общий метод для удаления файлов в S3
     * @param path - путь внутри бакета по которому удалить файл
     */
    void deleteFile(String path);
}
