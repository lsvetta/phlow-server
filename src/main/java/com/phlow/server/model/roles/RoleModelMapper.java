package com.phlow.server.model.roles;

import com.phlow.server.domain.common.ModelMapper;
import com.phlow.server.model.users.UserModelMapper;
import com.phlow.server.web.users.dto.RoleDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UserModelMapper.class)
public interface RoleModelMapper extends ModelMapper<RoleModel, RoleDto> {
}