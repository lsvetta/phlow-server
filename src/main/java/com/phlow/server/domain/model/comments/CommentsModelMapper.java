package com.phlow.server.domain.model.comments;

import com.phlow.server.domain.common.ModelMapper;
import com.phlow.server.web.comments.dto.CommentDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentsModelMapper extends ModelMapper<CommentModel, CommentDto> {
}
