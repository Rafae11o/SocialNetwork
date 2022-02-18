package com.socialNetwork.dto.post;

import com.socialNetwork.entities.post.Comment;
import lombok.Data;

@Data
public class CreateCommentRequest {
    private Long postId;
    private String text;
}
