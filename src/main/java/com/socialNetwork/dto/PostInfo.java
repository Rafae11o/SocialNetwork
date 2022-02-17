package com.socialNetwork.dto;

import com.socialNetwork.entities.Post;
import lombok.Data;

@Data
public class PostInfo {
    private String text;
    private Long id;

    public PostInfo(Post post){
        this.text = post.getText();
        this.id = post.getId();
    }

    public PostInfo(String text, Long id) {
        this.text = text;
        this.id = id;
    }

}
