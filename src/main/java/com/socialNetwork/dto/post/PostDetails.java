package com.socialNetwork.dto.post;

import com.socialNetwork.dto.post.comment.CommentInfo;
import com.socialNetwork.entities.post.Comment;
import com.socialNetwork.entities.post.Post;

import java.util.ArrayList;
import java.util.List;

public class PostDetails extends PostInfo{

    private List<CommentInfo> comments;
    private boolean enableComments;

    public PostDetails(Post post, List<Comment> comments) {
        super(post);
        this.enableComments = post.isEnableComments();
        this.comments = new ArrayList<>();
        for(Comment comment: comments){
            this.comments.add(new CommentInfo(comment));
        }
    }

    public List<CommentInfo> getComments() {
        return comments;
    }

    public boolean isEnableComments() {
        return enableComments;
    }
}
