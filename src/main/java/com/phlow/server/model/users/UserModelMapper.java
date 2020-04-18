package com.phlow.server.model.users;

import com.phlow.server.domain.common.ModelMapper;
import com.phlow.server.model.roles.RoleModelMapper;
import com.phlow.server.web.users.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = RoleModelMapper.class)
public interface UserModelMapper extends ModelMapper<UserModel, UserDto> {
}
