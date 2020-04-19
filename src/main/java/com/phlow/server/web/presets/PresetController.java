package com.phlow.server.web.presets;

import com.phlow.server.domain.model.presets.PresetsModelMapper;
import com.phlow.server.service.presets.PresetsService;
import com.phlow.server.service.presets.PresetsServiceImpl;
import com.phlow.server.web.presets.dto.PresetDto;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Presets")
@RestController
@RequestMapping(
        path = "${phlow.api.prefix:/api}" + "${phlow.api.versions.v1:/v1}" + "${phlow.api.mappings.presets:/presets}",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class PresetController {

    private final PresetsService presetsService;
    private final PresetsModelMapper presetsModelMapper;

    @Autowired
    public PresetController(PresetsService presetsService,
                            PresetsModelMapper presetsModelMapper) {
        this.presetsService = presetsService;
        this.presetsModelMapper = presetsModelMapper;
    }

    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PresetDto> getPresetByPost(@PathVariable final String postId) {
        return ResponseEntity.ok(this.presetsModelMapper.modelToDto(this.presetsService.getPresetByPostId(postId)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PresetDto> createPreset(@RequestBody PresetDto presetDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.presetsModelMapper.modelToDto(
                        this.presetsService.createPreset(this.presetsModelMapper.dtoToModel(presetDto))));
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PresetDto> updatePreset(
            @RequestBody PresetDto presetDto) {
        return ResponseEntity.ok(this.presetsModelMapper.modelToDto(
                        this.presetsService.updatePreset(this.presetsModelMapper.dtoToModel(presetDto))));
    }

    @DeleteMapping("/{presetId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity deletePreset(
            @PathVariable final String presetId) {
        this.presetsService.deletePreset(presetId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    }
}
