package com.socialNetwork.dto.user;

import com.socialNetwork.dto.post.PostInfo;
import com.socialNetwork.entities.post.Post;
import com.socialNetwork.entities.user.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserFeed {
    private UserInfo userInfo;
    private List<PostInfo> posts;

    public UserFeed(){}

    /**
     *
     * @param user - user model
     * @param posts - list of user's posts
     */
    public UserFeed(User user, List<Post> posts){
        userInfo = new UserInfo(user);
        this.posts = new ArrayList<>();
        for(Post post: posts){
            this.posts.add(new PostInfo(post));
        }
    }
}