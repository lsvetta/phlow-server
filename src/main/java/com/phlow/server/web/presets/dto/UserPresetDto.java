package com.phlow.server.web.presets.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.phlow.server.web.View;
import com.phlow.server.web.users.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonView(View.PUBLIC.class)
public class UserPresetDto implements Serializable {
    private static final long serialVersionUID = -6361675262832322659L;

    private UUID id;

    private UserDto user;

    private String name;
}
