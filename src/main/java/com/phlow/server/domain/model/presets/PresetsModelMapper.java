package com.phlow.server.domain.model.presets;

import com.phlow.server.domain.common.ModelMapper;
import com.phlow.server.web.presets.dto.PresetDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UserPresetsModelMapper.class)
public interface PresetsModelMapper extends ModelMapper<PresetModel, PresetDto> {
}
