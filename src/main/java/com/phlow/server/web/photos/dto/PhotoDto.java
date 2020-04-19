package com.phlow.server.web.photos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotoDto implements Serializable {
    private String id;
    private String imageLink;
}
