package com.socialNetwork.dto.post.comment;

import lombok.Data;

@Data
public class CreateCommentRequest {
    private Long postId;
    private String text;
}
