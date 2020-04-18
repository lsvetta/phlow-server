package com.phlow.server.service.users.roles;

import com.phlow.server.model.roles.RoleModel;
import com.phlow.server.model.roles.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<RoleModel> getAllRoles() {
        return this.roleRepository.findAll();
    }

    @Override
    public RoleModel getRole(String name) {
        return this.roleRepository.findByName(name);
    }

    @Override
    public RoleModel getRoleByUser(UUID userId0) {
        return null;
    }
}
