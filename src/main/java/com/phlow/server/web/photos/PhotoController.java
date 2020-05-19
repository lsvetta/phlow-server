package com.phlow.server.web.photos;

import com.phlow.server.domain.common.UploadFailureException;
import com.phlow.server.domain.model.photos.PhotosModelMapper;
import com.phlow.server.domain.model.users.UserModel;
import com.phlow.server.service.files.FileService;
import com.phlow.server.service.photos.PhotosService;
import com.phlow.server.web.photos.dto.PhotoDto;
import io.swagger.annotations.Api;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.util.UUID;

@Api(tags = "Photos")
@RestController
@RequestMapping(
        path = "${phlow.api.prefix:/api}" + "${phlow.api.versions.v1:/v1}" + "${phlow.api.mappings.photos:/photos}",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class PhotoController {

    private final PhotosService photosService;
    private final PhotosModelMapper photosModelMapper;

    @Autowired
    public PhotoController(PhotosService photosService,
                           PhotosModelMapper photosModelMapper) {
        this.photosService = photosService;
        this.photosModelMapper = photosModelMapper;
    }

    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PhotoDto> getPhotoByPost(@PathVariable final String postId) {
        return ResponseEntity.ok(this.photosModelMapper.modelToDto(this.photosService.getPhotoByPostId(postId)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<PhotoDto> createPhoto(@RequestPart(value = "photo") @NonNull final MultipartFile photo,
                                               @ApiIgnore @AuthenticationPrincipal UserModel userModel) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(this.photosModelMapper.modelToDto(this.photosService.createPhoto(photo, userModel.getId().toString())));
        } catch (IOException e) {
            throw new UploadFailureException(e.getMessage());
        }
    }

    @PatchMapping("/{photoId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<PhotoDto> updatePhoto(@RequestPart(value = "photo") @NonNull final MultipartFile photo,
                                                @PathVariable @NonNull final String photoId,
                                                @ApiIgnore @AuthenticationPrincipal UserModel userModel) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(this.photosModelMapper.modelToDto(this.photosService.updatePhoto(photo, userModel.getId().toString(), photoId)));
        } catch (IOException e) {
            throw new UploadFailureException(e.getMessage());
        }
    }

    @DeleteMapping("/{photoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Object> deletePhoto(
            @PathVariable final String photoId,
            @ApiIgnore @AuthenticationPrincipal UserModel userModel) {
        this.photosService.deletePhotoModel(photoId, userModel.getId().toString());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    }
}
