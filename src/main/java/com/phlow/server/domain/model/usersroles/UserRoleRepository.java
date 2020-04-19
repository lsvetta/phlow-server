package com.phlow.server.domain.model.usersroles;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRoleRepository extends CrudRepository<UserRoleModel, Integer> {
    List<UserRoleModel> findAllByUserId(UUID userId);
    UserRoleModel findFirstByUserId(UUID userId);
    void deleteUserRoleModelByUserId(UUID userId);
}
