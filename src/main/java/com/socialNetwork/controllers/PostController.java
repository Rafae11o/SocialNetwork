package com.socialNetwork.controllers;

import com.socialNetwork.dto.PostInfo;
import com.socialNetwork.dto.response.SuccessResponse;
import com.socialNetwork.security.CustomUserDetails;
import com.socialNetwork.services.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/post")
@Slf4j
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService){
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<SuccessResponse> createPost(@RequestBody PostInfo postInfo) throws Exception {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Long id = userDetails.getId();
        postService.createPost(id, postInfo);
        return ResponseEntity.ok(new SuccessResponse("Post created successfully"));
    }

    @GetMapping("/getAllPosts")
    public ResponseEntity<List<PostInfo>> getAllPosts() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Long id = userDetails.getId();
        List<PostInfo> posts = postService.findAllPosts(id);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/getPost")
    public ResponseEntity<PostInfo> getPost(@RequestParam Long id) throws Exception {
        PostInfo post = postService.findPost(id);
        return ResponseEntity.ok(post);
    }

    @PostMapping("/update")
    public ResponseEntity<PostInfo> updatePost(@RequestBody PostInfo postInfo) throws Exception {
        PostInfo updatedPost = postService.updatePost(postInfo);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping
    public ResponseEntity<SuccessResponse> deletePost(@RequestParam Long id){
        postService.deletePost(id);
        return ResponseEntity.ok(new SuccessResponse("Deleted successfully"));
    }
}
