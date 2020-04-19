package com.phlow.server.web.presets.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PresetDto implements Serializable {

    private static final long serialVersionUID = 3743951077773719360L;

    private UUID id;

    private String settings;
}
