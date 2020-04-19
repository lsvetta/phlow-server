package com.phlow.server.web.posts.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.phlow.server.web.comments.dto.CommentDto;
import com.phlow.server.web.photos.dto.PhotoDto;
import com.phlow.server.web.presets.dto.PresetDto;
import com.phlow.server.web.users.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto implements Serializable {
    private static final long serialVersionUID = -4739258564706008575L;

    private UUID id;
    private Date date;
    private String content;

    @JsonIgnoreProperties(value = "post")
    private List<CommentDto> comments;

    private PresetDto preset;
    private PhotoDto photo;
    private UserDto author;
}
