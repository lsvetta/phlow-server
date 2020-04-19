package com.phlow.server.domain.model.presets;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PresetsRepository extends CrudRepository<PresetModel, UUID> {
    void deletePresetModelById(String id);
}
