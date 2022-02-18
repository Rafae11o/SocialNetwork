package com.socialNetwork.dto.post.comment;

import com.socialNetwork.entities.post.Comment;
import lombok.Data;

@Data
public class CreateCommentRequest {
    private Long postId;
    private String text;
}
