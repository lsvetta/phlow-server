package com.phlow.server.service.users;

import com.phlow.server.domain.common.EntityNotFoundException;
import com.phlow.server.domain.common.InvalidArgumentException;
import com.phlow.server.model.roles.RoleModel;
import com.phlow.server.model.users.UserModel;
import com.phlow.server.model.users.UserModelMapper;
import com.phlow.server.model.users.UserRepository;
import com.phlow.server.service.users.roles.RoleService;
import com.phlow.server.service.users.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Value("${phlow.security.defaultRole:USER}")
    private String defaultRole;

    private final UserRepository userRepository;

    private final UserModelMapper userModelMapper;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    private final Validator validator;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserModelMapper userModelMapper, RoleService roleService, PasswordEncoder passwordEncoder, Validator validator) {
        this.userRepository = userRepository;
        this.userModelMapper = userModelMapper;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserModel> getAllUsers() {
        return this.userRepository.findAllByOrderByUsername();
    }

    /**
     * с поддержкой пейджинга
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserModel> findAll(int pageNumber, int pageSize) {
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize);
        return this.userRepository.findAllByOrderByUsername(pageRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public UserModel findUser(String usernameOrEmail) {
        return this.userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
    }

    @Override
    @Transactional
    public UserModel createUser(UserModel user) {
        this.validate(user, true);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        RoleModel role = this.roleService.getRole(this.defaultRole);
        if(role == null) {
            throw new EntityNotFoundException("Отсутствует роль по умолчанию: " + this.defaultRole);
        }
//        user.setId(UUID.randomUUID());
        user.setRoles(Collections.singletonList(role));
        user = this.userRepository.save(user);
        return user;
    }

    @Override
    @Transactional
    public UserModel updateUser(UserModel userModel) {
        UserModel user = this.userRepository.findUserModelById(userModel.getId());
        if(userModel.getRoles() != null) {
            List<RoleModel> roles = userModel.getRoles().stream().map(x -> this.roleService.getRole(x.getName().toUpperCase())).collect(Collectors.toList());
            if(roles != null && roles.size() > 0) {
                user.setRoles(roles);
            }
        }
        this.validate(userModel, false);
        if(userModel.getPassword() != null && !userModel.getPassword().isEmpty()) {
            user.setPassword(this.passwordEncoder.encode(userModel.getPassword()));
        }
        if (!userModel.getUsername().equals(user.getUsername())) {
            UserModel res = this.userRepository.findByUsername(userModel.getUsername());
            if (res != null && userModel.getId() != res.getId()) {
                throw new InvalidArgumentException("Пользователь с таким username уже сущесвтует");
            }
        }
        if (!userModel.getEmail().equals(user.getName())) {
            UserModel res = this.userRepository.findByUsernameOrEmail(userModel.getEmail(), userModel.getEmail());
            if (res != null && userModel.getId() != res.getId()) {
                throw new InvalidArgumentException("Пользователь с таким email уже сущесвтует");
            }
        }
        user.setEmail(userModel.getEmail());
        user.setName(userModel.getName());
        user.setUsername((userModel.getUsername()));

        return userRepository.save(user);
    }

    @Override
    public UserModel getUser(UUID id) {
        return this.userRepository.findUserModelById(id);
    }

    @Override
    public boolean validate(UserModel userModel, boolean onCreate) {
        if(userModel.getName().isEmpty()){
            throw new InvalidArgumentException("Имя не может быть пустым");
        }
        if(this.roleService.getAllRoles().isEmpty()) {
            throw new InvalidArgumentException("В БД нет ролей. Невозможно создать пользователя без роли.");
        }
        if(onCreate && this.userRepository.findByUsernameOrEmail(userModel.getUsername().toLowerCase(), userModel.getEmail().toLowerCase()) != null){
            throw new InvalidArgumentException("Пользователь с указанным email или логином уже существует");
        }
        if(!this.validator.validateEmail(userModel.getEmail())) {
            throw new InvalidArgumentException("Неверный формат email!");
        }
        if(!this.validator.validateUsername(userModel.getUsername())) {
            throw new InvalidArgumentException("Неверный формат username!");
        }
        if(onCreate && !this.validator.validatePassword(userModel.getPassword())) {
            throw new InvalidArgumentException("Неверный формат пароля!");
        }
        return true;
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        if(!this.isUserExists(id)) {
            throw new EntityNotFoundException("Пользователь не найден");
        }
        UserModel userModel = this.userRepository.findUserModelById(id);
        userModel.getRoles().forEach(role -> role.getUsers().remove(userModel));
        this.userRepository.delete(userModel);
    }

    @Override
    public boolean isUserExists(UUID id) {
        return userRepository.existsById(id);
    }
}
