package com.phlow.server.web.users;

import com.fasterxml.jackson.annotation.JsonView;
import com.phlow.server.domain.authentication.UserDetailsServiceImpl;
import com.phlow.server.domain.common.ActionForbiddenException;
import com.phlow.server.domain.common.EntityNotFoundException;
import com.phlow.server.domain.common.InvalidArgumentException;
import com.phlow.server.domain.common.UploadFailureException;
import com.phlow.server.domain.model.users.UserModel;
import com.phlow.server.domain.model.users.UserModelMapper;
import com.phlow.server.service.users.UserService;
import com.phlow.server.web.View;
import com.phlow.server.web.photos.dto.PhotoDto;
import com.phlow.server.web.users.dto.UserDto;
import io.swagger.annotations.Api;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Api(tags = "Users")
@RestController
@RequestMapping(
        path = "${phlow.api.prefix:/api}" + "${phlow.api.versions.v1:/v1}" + "${phlow.api.mappings.users:/users}",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class UserController {

    private final UserService userService;
    private final UserModelMapper userModelMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, UserModelMapper userModelMapper, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userModelMapper = userModelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @JsonView(View.PUBLIC.class)
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                this.userModelMapper.modelToDto(
                        this.userService.createUser(this.userModelMapper.dtoToModel(userDto)))
        );
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<UserDto> setAvatar(@RequestPart(value = "photo") @NonNull final MultipartFile photo,
                                                @ApiIgnore @AuthenticationPrincipal UserModel userModel) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(this.userModelMapper.modelToDto(this.userService.setAvatar(photo, userModel.getId())));
        } catch (IOException e) {
            throw new UploadFailureException(e.getMessage());
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @JsonView(View.PUBLIC.class)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok().body(this.userModelMapper.modelsToDtos(this.userService.getAllUsers()));
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/me")
    @JsonView(View.PUBLIC.class)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<UserDto> getInformationAboutCurrentUser(@AuthenticationPrincipal
                                                                  @ApiIgnore UserModel userModel) {
        return ResponseEntity.ok().body(this.userModelMapper.modelToDto(this.userService.getUser(userModel.getId())));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity deleteUser(@PathVariable(value = "id") UUID id) {
        if (!this.userService.isUserExists(id)) {
            throw new EntityNotFoundException("Пользователь не найден");
        }
        this.userService.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{idOrUsername}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<UserDto> getUserbyIdOrUsername(@PathVariable(value = "idOrUsername") String idOrUsername) {
        final UserDto userDto = this.userModelMapper.modelToDto(userService.getUserByIdOrUsername(idOrUsername));
        if(userDto == null) {
            throw new EntityNotFoundException("Пользователь не найден");
        }
        return ResponseEntity.ok(userDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/session")
    public ResponseEntity<Boolean> getIfLoggedInUser(@AuthenticationPrincipal @ApiIgnore UserModel userModel) {
        return userModel == null ? ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Boolean.FALSE) : ResponseEntity.ok(Boolean.TRUE);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/update/{id}")
    @JsonView(View.PUBLIC.class)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<UserDto> updateUser(@PathVariable(value = "id") UUID id, @RequestBody UserDto userDto,
                                              @AuthenticationPrincipal @ApiIgnore UserModel userModel) {
        if (userModel.getRoles().stream().noneMatch(x -> x.getName().equals("ADMIN"))) {
            if (!userModel.getId().equals(id)) {
                throw new ActionForbiddenException("Недостаточно прав");
            }
            if (userDto.getRoles() != null) {
                if (!userDto.getRoles().isEmpty() && userDto.getRoles().stream().anyMatch((x -> !x.getName().equalsIgnoreCase("USER")))) {
                    throw new ActionForbiddenException("Недостаточно прав");
                }
            }
        }
        UserModel actual = this.userService.getUser(id);

        if (userModel.getRoles().stream().noneMatch(x -> x.getName().equals("ADMIN")) &&
                !this.passwordEncoder.matches(userDto.getPassword(), actual.getPassword())) {
            throw new InvalidArgumentException("Неправильный пароль");
        }
        if (actual == null) {
            throw new InvalidArgumentException("Пользователь не найден");
        }
        if ((userDto.getNewPassword() != null &&
                !userDto.getNewPassword().isEmpty()) || (userDto.getNewPasswordRepeat() != null
                && !userDto.getNewPasswordRepeat().isEmpty())) {
            if (!userDto.getNewPassword().equals(userDto.getNewPasswordRepeat())) {
                throw new InvalidArgumentException("Пароли не совпадают");
            }
            userDto.setPassword(userDto.getNewPassword());
        }
        userDto.setId(actual.getId());
        return ResponseEntity.ok().body(this.userModelMapper.modelToDto(this.userService.updateUser(this.userModelMapper.dtoToModel(userDto))));
    }
}
