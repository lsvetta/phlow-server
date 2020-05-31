package com.phlow.server.domain.model.presets;

import com.phlow.server.domain.common.ModelMapper;
import com.phlow.server.domain.model.comments.CommentsModelMapper;
import com.phlow.server.domain.model.photos.PhotosModelMapper;
import com.phlow.server.domain.model.roles.RoleModelMapper;
import com.phlow.server.domain.model.users.UserModelMapper;
import com.phlow.server.web.presets.dto.PresetDto;
import com.phlow.server.web.presets.dto.UserPresetDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses ={UserModelMapper.class, PresetsModelMapper.class})
public interface UserPresetsModelMapper extends ModelMapper<UserPresetModel, UserPresetDto> {
}
