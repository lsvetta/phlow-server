package com.phlow.server.service.presets;

import com.phlow.server.domain.model.presets.PresetModel;
import com.phlow.server.domain.model.presets.UserPresetModel;
import com.phlow.server.web.presets.dto.UserPresetDto;

import java.util.List;
import java.util.UUID;

public interface PresetsService {
    public PresetModel createPreset(final PresetModel presetModel, final UUID userId, final String name);
    public PresetModel addPreset(UserPresetModel userPresetModel);
    public List<PresetModel> getPresetsForUser(UUID userId);
    public List<UserPresetModel> getUserPresetsForUser(UUID userId);
    public UserPresetModel updatePresetName(final UserPresetModel newPreset);
    public void deleteUserPreset(final UUID userId, final String id);
    public PresetModel getPresetByPostId(final String postId);
}
