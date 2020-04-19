package com.phlow.server.domain.model.photos;

import com.phlow.server.domain.common.ModelMapper;
import com.phlow.server.web.photos.dto.PhotoDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PhotosModelMapper extends ModelMapper<PhotoModel, PhotoDto> {
}
