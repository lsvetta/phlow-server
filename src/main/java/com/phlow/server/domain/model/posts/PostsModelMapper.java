package com.phlow.server.domain.model.posts;

import com.phlow.server.domain.common.ModelMapper;
import com.phlow.server.domain.model.comments.CommentsModelMapper;
import com.phlow.server.domain.model.photos.PhotosModelMapper;
import com.phlow.server.domain.model.users.UserModelMapper;
import com.phlow.server.web.posts.dto.PostDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PhotosModelMapper.class, CommentsModelMapper.class, UserModelMapper.class})
public interface PostsModelMapper extends ModelMapper<PostModel, PostDto> {
}
