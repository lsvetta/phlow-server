package com.phlow.server.domain.model.roles;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends CrudRepository<RoleModel, Integer> {
    List<RoleModel> findAll();
    RoleModel findByName(String name);
}
