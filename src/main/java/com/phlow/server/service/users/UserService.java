package com.phlow.server.service.users;

import com.phlow.server.domain.model.users.UserModel;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserModel> getAllUsers();
    List<UserModel> findAll(int pageNumber, int pageSize);
    UserModel findUser(String usernameOrEmail);
    UserModel createUser(UserModel userModel);
    UserModel updateUser(UserModel userModel);
    UserModel getUser(UUID id);
    boolean validate(UserModel userModel, boolean onCreate);
    void deleteById(UUID id);
    boolean isUserExists(UUID id);
}
