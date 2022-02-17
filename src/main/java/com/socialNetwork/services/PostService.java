package com.socialNetwork.services;

import com.socialNetwork.dto.PostInfo;
import com.socialNetwork.entities.Post;
import com.socialNetwork.entities.user.User;
import com.socialNetwork.exceptions.DeveloperException;
import com.socialNetwork.repositories.PostRepository;
import com.socialNetwork.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createPost(Long owner_id, PostInfo postInfo) throws Exception {
        User user = userRepository.findById(owner_id).orElseThrow(() -> {
            String info = "Owner_id: " + owner_id + " PostID: " + postInfo.getId();
            return new DeveloperException("PostService createPost", info);
        });
        Post post = new Post(postInfo.getText(), user);
        postRepository.save(post);
    }

    public PostInfo findPost(Long id) throws Exception {
        Post post = postRepository.findById(id).orElseThrow(() -> {
            String info = "Post id: " + id;
            return new DeveloperException("PostService findPost", info);
        });
        return new PostInfo(post);
    }

    public List<PostInfo> findAllPosts(Long id) {
        List<Post> userPosts = postRepository.findByOwnerId(id);
        List<PostInfo> postInfoList = new ArrayList<>();
        for(Post post : userPosts){
            postInfoList.add(new PostInfo(post));
        }
        return postInfoList;
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    @Transactional
    public PostInfo updatePost(PostInfo postInfo) throws Exception {
        Post post = postRepository.findById(postInfo.getId()).orElseThrow(() -> {
            String info = "Post id: " + postInfo.getId();
            return new DeveloperException("PostService updatePost", info);
        });
        post.setText(postInfo.getText());
        postRepository.save(post);
        return postInfo;
    }
}
