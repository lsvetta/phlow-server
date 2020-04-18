package com.phlow.server.service.users.roles;

import com.phlow.server.model.roles.RoleModel;

import java.util.List;
import java.util.UUID;

public interface RoleService {
    List<RoleModel> getAllRoles();
    RoleModel getRole(String name);
    RoleModel getRoleByUser(UUID userId);
}
