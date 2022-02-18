package com.socialNetwork.entities.post;

import com.socialNetwork.dto.post.CreatePostRequest;
import com.socialNetwork.entities.BaseEntity;
import com.socialNetwork.entities.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@NoArgsConstructor
@Data
public class Post extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    private String text;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "post")
    private List<Comment> comments;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostVisionPermission postVisionPermission;

    private boolean enableComments;

    public Post(User owner, CreatePostRequest postInfo) {
        this.owner = owner;
        this.text = postInfo.getText();
        this.postVisionPermission = postInfo.getPermission();
        this.enableComments = postInfo.isEnableComments();
        comments = new ArrayList<>();
    }


}
