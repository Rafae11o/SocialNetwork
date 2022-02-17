package com.socialNetwork.entities;

import com.socialNetwork.entities.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "posts")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Post extends BaseEntity {

    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

}
