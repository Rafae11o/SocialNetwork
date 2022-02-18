package com.socialNetwork.dto.post;

import com.socialNetwork.dto.user.UserInfo;
import com.socialNetwork.entities.post.Post;
import lombok.Data;

import java.util.Date;

@Data
public class PostInfo {
    private String text;
    private Long id;
    private UserInfo owner;
    private Date created;

    public PostInfo(Post post){
        this.text = post.getText();
        this.id = post.getId();
        this.owner = new UserInfo(post.getOwner());
        this.created = post.getCreated();
    }

}
