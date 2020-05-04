package com.phlow.server.web.photos.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.phlow.server.web.View;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonView(View.PUBLIC.class)
public class PhotoDto implements Serializable {
    private static final long serialVersionUID = -2919527573524188206L;

    private UUID id;
    private String imageLink;
}
