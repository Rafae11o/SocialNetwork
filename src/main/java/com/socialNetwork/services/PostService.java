package com.socialNetwork.services;

import com.socialNetwork.dto.post.CreatePostRequest;
import com.socialNetwork.dto.post.EditRequest;
import com.socialNetwork.dto.post.PostDetails;
import com.socialNetwork.dto.post.PostInfo;
import com.socialNetwork.entities.post.Comment;
import com.socialNetwork.entities.post.Post;
import com.socialNetwork.entities.post.PostVisionPermission;
import com.socialNetwork.entities.user.User;
import com.socialNetwork.exceptions.DeveloperException;
import com.socialNetwork.repositories.CommentRepository;
import com.socialNetwork.repositories.PostRepository;
import com.socialNetwork.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final String LOG_TAG;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        LOG_TAG = this.getClass().getName();
    }

    @Transactional
    public PostInfo createPost(Long owner_id, CreatePostRequest postInfo) throws Exception {
        User user = userRepository.findById(owner_id).orElseThrow(() -> {
            String info = "Owner_id: " + owner_id;
            return new DeveloperException("PostService createPost", info);
        });
        Post post = new Post(user, postInfo);
        post = postRepository.save(post);
        return new PostInfo(post);
    }

    @Transactional
    public PostDetails findPost(Long id) throws Exception {
        Post post = postRepository.findById(id).orElseThrow(() -> {
            String info = "Post id: " + id;
            return new DeveloperException("PostService findPost", info);
        });
        List<Comment> comments = commentRepository.findAllByPostId(id);
        return new PostDetails(post, comments);
    }

    public List<PostInfo> findAllPosts(Long id) {
        List<Post> userPosts = postRepository.findByOwnerId(id);
        List<PostInfo> posts = new ArrayList<>();
        for(Post post : userPosts){
            posts.add(new PostInfo(post));
        }
        return posts;
    }

    public void deletePost(Long userId, Long postId) throws DeveloperException {
        Post post = postRepository.findById(postId).orElseThrow(() -> {
            String info = "Post id: " + postId;
            return new DeveloperException("PostService deletePost", info);
        });
        if(!Objects.equals(post.getOwner().getId(), userId)) {
            throw new DeveloperException(LOG_TAG + " updatePost", "Illegal request");
        }
        postRepository.deleteById(postId);
    }

    @Transactional
    public PostInfo updatePost(Long userId, EditRequest editRequest) throws Exception {
        Post post = postRepository.findById(editRequest.getId()).orElseThrow(() -> {
            String info = "Post id: " + editRequest.getId();
            return new DeveloperException("PostService updatePost", info);
        });
        if(!Objects.equals(post.getOwner().getId(), userId)) {
            throw new DeveloperException(LOG_TAG + " updatePost", "Illegal request");
        }
        post.setText(editRequest.getText());
        return new PostInfo(postRepository.save(post));
    }

    public List<String> getAvailablePermission() {
        return PostVisionPermission.names();
    }
}
