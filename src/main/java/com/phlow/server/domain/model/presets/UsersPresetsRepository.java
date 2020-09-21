package com.phlow.server.domain.model.presets;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UsersPresetsRepository extends CrudRepository<UserPresetModel, UUID> {
    UserPresetModel findFirstById(UUID id);
    List<UserPresetModel> findAllByUserId(UUID id);
    boolean existsByUserIdAndPresetId(UUID user, UUID preset);
    UserPresetModel findByUserIdAndPresetId(UUID user, UUID preset);
    UserPresetModel findByUserIdAndId(UUID user, UUID presetID);
    void deleteByUserIdAndId(UUID user, UUID id);
}
