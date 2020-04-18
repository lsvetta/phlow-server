package com.phlow.server.model.users;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserModel, UUID>,
        PagingAndSortingRepository<UserModel, UUID> {
    List<UserModel> findAllByOrderByUsername();

    List<UserModel> findAllByOrderByUsername(Pageable pageable);

    UserModel findUserModelById(UUID id);

    void deleteUserModelById(UUID id);

    UserModel findByUsername(String username);

    UserModel findByUsernameOrEmail(String username, String email);
}
