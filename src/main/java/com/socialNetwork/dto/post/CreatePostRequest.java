package com.socialNetwork.dto.post;

import com.socialNetwork.entities.post.PostVisionPermission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreatePostRequest {
    private String text;
    private PostVisionPermission permission;
    private boolean enableComments;
}
