package com.phlow.server.web.presets.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.phlow.server.web.View;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonView(View.PUBLIC.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PresetDto implements Serializable {

    private static final long serialVersionUID = 3743951077773719360L;

    private UUID id;

    private String settings;
    @JsonIgnoreProperties(value = "preset")
    private List<UserPresetDto> users;
}
