package com.phlow.server.web.photos;

import com.phlow.server.domain.model.photos.PhotosModelMapper;
import com.phlow.server.domain.model.presets.PresetsModelMapper;
import com.phlow.server.service.photos.PhotosService;
import com.phlow.server.service.presets.PresetsService;
import com.phlow.server.web.photos.dto.PhotoDto;
import com.phlow.server.web.presets.dto.PresetDto;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<PhotoDto> createPhoto(@RequestBody PhotoDto photoDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.photosModelMapper.modelToDto(
                        this.photosService.createPhoto(this.photosModelMapper.dtoToModel(photoDto))));
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PhotoDto> updatePhoto(
            @RequestBody PhotoDto photoDto) {
        return ResponseEntity.ok(this.photosModelMapper.modelToDto(
                this.photosService.updatePhoto(this.photosModelMapper.dtoToModel(photoDto))));
    }

    @DeleteMapping("/{photoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity deletePhoto(
            @PathVariable final String photoId) {
        this.photosService.deletePhotoModel(photoId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    }
}
