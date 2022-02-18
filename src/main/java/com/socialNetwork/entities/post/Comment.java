package com.socialNetwork.entities.post;

import com.socialNetwork.dto.post.comment.CreateCommentRequest;
import com.socialNetwork.entities.BaseEntity;
import com.socialNetwork.entities.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "comments")
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends BaseEntity {

    private String text;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public Comment(User user, Post post, CreateCommentRequest commentInfo) {
        this.owner = user;
        this.text = commentInfo.getText();
        this.post = post;
    }

}
