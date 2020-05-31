package com.phlow.server.service.presets;

import com.phlow.server.domain.model.posts.PostModel;
import com.phlow.server.domain.model.posts.PostsRepository;
import com.phlow.server.domain.model.presets.PresetModel;
import com.phlow.server.domain.model.presets.PresetsRepository;
import com.phlow.server.domain.model.presets.UserPresetModel;
import com.phlow.server.domain.model.presets.UsersPresetsRepository;
import com.phlow.server.domain.model.users.UserModel;
import com.phlow.server.domain.model.users.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PresetsServiceImpl implements PresetsService {

    private final PresetsRepository presetsRepository;
    private final UsersPresetsRepository usersPresetsRepository;
    private final UserRepository userRepository;
    private final PostsRepository postsRepository;

    @Autowired
    public PresetsServiceImpl(PresetsRepository presetsRepository,
                              PostsRepository postsRepository,
                              UserRepository userRepository,
                              UsersPresetsRepository usersPresetsRepository) {
        this.presetsRepository = presetsRepository;
        this.postsRepository = postsRepository;
        this.userRepository = userRepository;
        this.usersPresetsRepository = usersPresetsRepository;
    }

    @Override
    @Transactional
    public PresetModel createPreset(final PresetModel presetModel, final UUID userId, final String name) {
        UserModel user = this.userRepository.findUserModelById(userId);
        presetModel.setUsers(Collections.singletonList(new UserPresetModel(null, user, presetModel, name)));
        return this.presetsRepository.save(presetModel);
    }

    @Override
    public PresetModel addPreset(UserPresetModel userPresetModel) {
        if(this.usersPresetsRepository.existsByUserIdAndPresetId(userPresetModel.getUser().getId(), userPresetModel.getPreset().getId())){
            return this.usersPresetsRepository.findByUserIdAndPresetId(userPresetModel.getUser().getId(), userPresetModel.getPreset().getId()).getPreset();
        }
        userPresetModel.setPreset(this.presetsRepository.findFirstById(userPresetModel.getPreset().getId()));
        userPresetModel.setUser(this.userRepository.findUserModelById(userPresetModel.getUser().getId()));
        return this.usersPresetsRepository.save(userPresetModel).getPreset();
    }

    @Override
    public List<PresetModel> getPresetsForUser(UUID userId) {
        return this.usersPresetsRepository.findAllByUserId(userId).stream().map(UserPresetModel::getPreset).collect(Collectors.toList());
    }

    @Override
    public List<UserPresetModel> getUserPresetsForUser(UUID userId) {
        return this.usersPresetsRepository.findAllByUserId(userId);
    }

    @Override
    public PresetModel updatePresetName(UserPresetModel newPreset) {
        UserPresetModel preset = this.usersPresetsRepository.findByUserIdAndPresetId(newPreset.getUser().getId(), newPreset.getPreset().getId());
        preset.setName(newPreset.getName());
        return this.presetsRepository.findFirstById(this.usersPresetsRepository.save(preset).getPreset().getId());
    }

//    @Override
//    @Transactional
//    public void deletePreset(final String id) {
//        this.presetsRepository.deleteById(UUID.fromString(id));
//    }

    @Override
    public PresetModel getPresetByPostId(final String postId) {
        PostModel post = this.postsRepository.findFirstById(UUID.fromString(postId));
        if(post != null){
            return post.getPreset();
        }
        else return null;
    }
}
