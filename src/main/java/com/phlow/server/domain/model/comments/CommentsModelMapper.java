package com.phlow.server.domain.model.comments;

import com.phlow.server.domain.common.ModelMapper;
import com.phlow.server.domain.model.posts.PostsModelMapper;
import com.phlow.server.domain.model.users.UserModelMapper;
import com.phlow.server.web.comments.dto.CommentDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PostsModelMapper.class, UserModelMapper.class})
public interface CommentsModelMapper extends ModelMapper<CommentModel, CommentDto> {
}
