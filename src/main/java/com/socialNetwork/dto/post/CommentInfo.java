package com.socialNetwork.dto.post;

import com.socialNetwork.dto.UserInfo;
import com.socialNetwork.entities.post.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class CommentInfo {

    private Long id;
    private UserInfo ownerInfo;
    private Date created;
    private String text;

    public CommentInfo(Comment comment) {
        this.id = comment.getId();
        this.ownerInfo = new UserInfo(comment.getOwner());
        this.created = comment.getCreated();
        this.text = comment.getText();
    }
}
