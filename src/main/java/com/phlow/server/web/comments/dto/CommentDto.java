package com.phlow.server.web.comments.dto;

import com.phlow.server.web.posts.dto.PostDto;
import com.phlow.server.web.users.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto implements Serializable {
    private static final long serialVersionUID = -6725564388932756179L;

    private UUID id;

    private UserDto author;
    private PostDto post;
    private String content;
    private Date dateCommented;
}
