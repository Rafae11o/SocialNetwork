package com.socialNetwork.services;

import com.socialNetwork.dto.post.comment.CommentInfo;
import com.socialNetwork.dto.post.EditRequest;
import com.socialNetwork.dto.post.comment.CreateCommentRequest;
import com.socialNetwork.entities.post.Comment;
import com.socialNetwork.entities.post.Post;
import com.socialNetwork.entities.user.User;
import com.socialNetwork.exceptions.DeveloperException;
import com.socialNetwork.repositories.CommentRepository;
import com.socialNetwork.repositories.PostRepository;
import com.socialNetwork.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final String LOG_TAG;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        LOG_TAG = this.getClass().getName();
    }

    /**
     * Create comment and return created comment by CommentInfo object
     * @param userId - userId who create post
     * @param commentInfo - comment info
     * @return CommentInfo object
     * @throws DeveloperException
     */
    @Transactional
    public CommentInfo create(Long userId, CreateCommentRequest commentInfo) throws DeveloperException {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            String info = "User with id " + userId + " not founded";
            return new DeveloperException(LOG_TAG + " " + "create", info);
        });
        Post post = postRepository.findById(commentInfo.getPostId()).orElseThrow(() -> {
            String info = "Post with id " + userId + " not founded";
            return new DeveloperException(this.getClass().getName() + " " + "create", info);
        });
        if(!post.isEnableComments()) {
            throw new DeveloperException(LOG_TAG + " create", "Illegal comment creation");
        }
        Comment comment = new Comment(user, post, commentInfo);
        comment = this.commentRepository.save(comment);
        return new CommentInfo(comment);
    }

    /**
     *
     * @param userId - who delete comment
     * @param commentId
     * @throws DeveloperException
     */
    public void delete(Long userId, Long commentId) throws DeveloperException {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> {
            String info = "Comment with id " + commentId + " not founded";
            return new DeveloperException(LOG_TAG + " edit", info);
        });
        if(!Objects.equals(comment.getOwner().getId(), userId)) {
            throw new DeveloperException(LOG_TAG + " updatePost", "Illegal request");
        }
        commentRepository.deleteById(commentId);
    }

    /**
     *
     * @param userId - who editing comment
     * @param commentEditRequest
     * @return updated text of comment
     * @throws DeveloperException
     */
    @Transactional
    public String edit(Long userId, EditRequest commentEditRequest) throws DeveloperException {
        Comment comment = commentRepository.findById(commentEditRequest.getId()).orElseThrow(() -> {
            String info = "Comment with id " + commentEditRequest.getId() + " not founded";
            return new DeveloperException(LOG_TAG + " edit", info);
        });
        if(!Objects.equals(comment.getOwner().getId(), userId)) {
            throw new DeveloperException(LOG_TAG + " updatePost", "Illegal request");
        }
        comment.setText(commentEditRequest.getText());
        commentRepository.save(comment);
        return commentEditRequest.getText();
    }

    /**
     * Get comments by post id
     * @param postId - id of the post, comments of which needs to return
     * @return List of CommentInfo objects
     */
    public List<CommentInfo> getComments(Long postId) {
        List<Comment> commentsData = commentRepository.findAllByPostId(postId);
        List<CommentInfo> comments = new ArrayList<>();
        for(Comment comment: commentsData){
            comments.add(new CommentInfo(comment));
        }
        return comments;
    }


}
