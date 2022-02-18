package com.socialNetwork.entities.post;

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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    private String text;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    private List<Comment> comments;

    public Post(User owner, String text) {
        this.owner = owner;
        this.text = text;
        comments = new ArrayList<>();
    }


}
