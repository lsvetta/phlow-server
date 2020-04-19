package com.phlow.server.service.presets;

import com.phlow.server.domain.model.presets.PresetModel;

import java.util.UUID;

public interface PresetsService {
    public PresetModel createPreset(final PresetModel presetModel);
    public PresetModel updatePreset(final PresetModel presetModel);
    public void deletePreset(final String id);
    public PresetModel getPresetByPostId(final String postId);
}
