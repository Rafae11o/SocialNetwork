package com.socialNetwork.dto.post;

import com.socialNetwork.entities.post.PostVisionPermission;
import lombok.Data;

@Data
public class CreatePostRequest {
    private String text;
    private PostVisionPermission permission;
    private boolean enableComments;
}
