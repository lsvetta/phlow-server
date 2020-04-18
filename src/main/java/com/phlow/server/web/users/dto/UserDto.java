package com.phlow.server.web.users.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.phlow.server.web.View;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

        private UUID id;

        @JsonView(View.PUBLIC.class)
        private String name;

        @JsonView(View.PUBLIC.class)
        private String email;

        @JsonView(View.PUBLIC.class)
        private String username;

        @JsonView(View.PRIVATE.class)
        private String password;

        @JsonView(View.MANAGEMENT.class)
        private String newPassword;

        @JsonView(View.MANAGEMENT.class)
        private String newPasswordRepeat;

        @JsonView(View.PUBLIC.class)
        private List<RoleDto> roles;
}
