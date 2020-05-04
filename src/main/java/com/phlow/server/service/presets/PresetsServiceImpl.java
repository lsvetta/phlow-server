package com.phlow.server.service.presets;

import com.phlow.server.domain.model.posts.PostsRepository;
import com.phlow.server.domain.model.presets.PresetModel;
import com.phlow.server.domain.model.presets.PresetsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class PresetsServiceImpl implements PresetsService {

    private final PresetsRepository presetsRepository;
    private final PostsRepository postsRepository;

    @Autowired
    public PresetsServiceImpl(PresetsRepository presetsRepository,
                              PostsRepository postsRepository) {
        this.presetsRepository = presetsRepository;
        this.postsRepository = postsRepository;
    }

    @Override
    public PresetModel createPreset(final PresetModel presetModel) {
        return this.presetsRepository.save(presetModel);
    }

    @Override
    public PresetModel updatePreset(final PresetModel presetModel) {
        return this.presetsRepository.save(presetModel);
    }

    @Override
    public void deletePreset(final String id) {
        this.presetsRepository.deleteById(UUID.fromString(id));
    }

    @Override
    public PresetModel getPresetByPostId(final String postId) {
        return this.postsRepository.findFirstById(UUID.fromString(postId)).getPreset();
    }
}
