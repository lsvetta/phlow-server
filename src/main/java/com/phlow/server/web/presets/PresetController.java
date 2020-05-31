package com.phlow.server.web.presets;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.phlow.server.domain.model.presets.*;
import com.phlow.server.domain.model.users.UserModel;
import com.phlow.server.domain.model.users.UserModelMapper;
import com.phlow.server.service.presets.PresetsService;
import com.phlow.server.service.presets.PresetsServiceImpl;
import com.phlow.server.web.View;
import com.phlow.server.web.presets.dto.PresetDto;
import com.phlow.server.web.presets.dto.UserPresetDto;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Api(tags = "Presets")
@RestController
@RequestMapping(
        path = "${phlow.api.prefix:/api}" + "${phlow.api.versions.v1:/v1}" + "${phlow.api.mappings.presets:/presets}",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class PresetController {

    private final PresetsService presetsService;
    private final PresetsModelMapper presetsModelMapper;
    private final UserPresetsModelMapper userPresetsModelMapper;
    private final GetUserPresetsModelMapper getUserPresetsModelMapper;
    private final UserModelMapper userModelMapper;

    @Autowired
    public PresetController(PresetsService presetsService,
                            PresetsModelMapper presetsModelMapper,
                            UserPresetsModelMapper userPresetsModelMapper,
                            UserModelMapper userModelMapper, GetUserPresetsModelMapper getUserPresetsModelMapper) {
        this.presetsService = presetsService;
        this.presetsModelMapper = presetsModelMapper;
        this.userPresetsModelMapper = userPresetsModelMapper;
        this.userModelMapper = userModelMapper;
        this.getUserPresetsModelMapper = getUserPresetsModelMapper;
    }

    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(View.PUBLIC.class)
    public ResponseEntity<PresetDto> getPresetByPost(@PathVariable final String postId) {
        return ResponseEntity.ok(this.presetsModelMapper.modelToDto(this.presetsService.getPresetByPostId(postId)));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @JsonView(View.PUBLIC.class)
    public ResponseEntity<List<PresetDto>> getPresetsByUser(@AuthenticationPrincipal @ApiIgnore final UserModel currentUser) {
        return ResponseEntity.ok(
                this.presetsModelMapper.modelsToDtos(this.presetsService.getPresetsForUser(currentUser.getId()))
                        .stream().map(dto -> {
                    dto.setUsers(dto.getUsers().stream().map(usersPreset -> {
                        usersPreset.setUser(this.userModelMapper.modelToDto(currentUser));
                        return usersPreset;
                    }).collect(Collectors.toList()));
                    return dto;
                }).collect(Collectors.toList()));
    }

    @PostMapping("/add/{presetId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @JsonView(View.PUBLIC.class)
    public ResponseEntity<PresetDto> sharePreset(@PathVariable final String presetId,
            @RequestBody final UserPresetDto userPresetDto,
                                                  @AuthenticationPrincipal @ApiIgnore final UserModel currentUser) {

        userPresetDto.setUser(this.userModelMapper.modelToDto(currentUser));
        UserPresetModel model = this.userPresetsModelMapper.dtoToModel(userPresetDto);
        model.setPreset(new PresetModel(UUID.fromString(presetId), null, null, Collections.singletonList(model)));
        PresetDto preset = this.presetsModelMapper.modelToDto(this.presetsService.addPreset(model));
        preset.setUsers(preset.getUsers().stream().filter(
                u -> u.getUser().getEmail().equals(currentUser.getEmail())).collect(Collectors.toList()));
        return ResponseEntity.status(HttpStatus.CREATED).body(preset);
    }

    @PostMapping("/{name}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @JsonView(View.PUBLIC.class)
    public ResponseEntity<PresetDto> createPreset(@PathVariable(value = "name") final String name,
            @RequestBody final PresetDto presetDto,
                                                  @AuthenticationPrincipal @ApiIgnore final UserModel currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.presetsModelMapper.modelToDto(this.presetsService.createPreset(
                        this.presetsModelMapper.dtoToModel(presetDto), currentUser.getId(), name)));
    }

    @GetMapping("/userpresets")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @JsonView(View.PUBLIC.class)
    public ResponseEntity<List<GetUserPresetDto>> getUserPresetsByUser(@AuthenticationPrincipal @ApiIgnore final UserModel currentUser) {
        return ResponseEntity.ok(this.getUserPresetsModelMapper.modelsToDtos(
                this.presetsService.getUserPresetsForUser(currentUser.getId())).stream().map(p -> {
            p.getPreset().setUsers(null);
            return p;
        }).collect(Collectors.toList()));
    }

    @PatchMapping("/{presetId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @JsonView(View.PUBLIC.class)
    public ResponseEntity<PresetDto> updatePreset(
            @PathVariable(value = "presetId") final String presetId,
            @RequestBody final UserPresetDto userPresetDto,
            @AuthenticationPrincipal @ApiIgnore final UserModel currentUser) {
        userPresetDto.setUser(this.userModelMapper.modelToDto(currentUser));
        UserPresetModel preset = this.userPresetsModelMapper.dtoToModel(userPresetDto);
        preset.setPreset(new PresetModel());
        preset.getPreset().setId(UUID.fromString(presetId));
        return ResponseEntity.ok(this.presetsModelMapper.modelToDto(
                        this.presetsService.updatePresetName(preset)));
    }

//    @DeleteMapping("/{presetId}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @PreAuthorize("hasAuthority('USER')")
//    public ResponseEntity deletePreset(
//            @PathVariable final String presetId) {
//        this.presetsService.deletePreset(presetId);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
//    }
}
