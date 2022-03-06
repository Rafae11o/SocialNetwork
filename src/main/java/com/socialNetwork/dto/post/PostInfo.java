package com.socialNetwork.dto.post;

import com.socialNetwork.dto.user.UserInfo;
import com.socialNetwork.entities.post.Post;
import com.socialNetwork.entities.post.PostVisionPermission;
import lombok.Data;

import java.util.Date;

@Data
public class PostInfo {
    private String text;
    private Long id;
    private UserInfo owner;
    private PostVisionPermission postVisionPermission;
    private boolean enableComments;
    private Date created;

    public PostInfo(Post post){
        this.text = post.getText();
        this.id = post.getId();
        this.owner = new UserInfo(post.getOwner());
        this.postVisionPermission = post.getPostVisionPermission();
        this.enableComments = post.isEnableComments();
        this.created = post.getCreated();
    }

}
