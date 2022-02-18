package com.socialNetwork.controllers;

import com.socialNetwork.dto.post.CreatePostRequest;
import com.socialNetwork.dto.post.EditRequest;
import com.socialNetwork.dto.post.PostDetails;
import com.socialNetwork.dto.post.PostInfo;
import com.socialNetwork.dto.response.SuccessResponse;
import com.socialNetwork.dto.response.SuccessResponseWithData;
import com.socialNetwork.exceptions.DeveloperException;
import com.socialNetwork.security.CustomUserDetails;
import com.socialNetwork.services.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<SuccessResponseWithData<PostInfo>> createPost(@RequestBody CreatePostRequest createPostRequest) throws Exception {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Long id = userDetails.getId();
        PostInfo postInfo = postService.createPost(id, createPostRequest);
        return ResponseEntity.ok(new SuccessResponseWithData<>(postInfo));
    }

    @GetMapping("/getPost")
    public ResponseEntity<SuccessResponseWithData<PostDetails>> getPost(@RequestParam Long id) throws Exception {
        PostDetails post = postService.findPost(id);
        return ResponseEntity.ok(new SuccessResponseWithData<>(post));
    }

    @PostMapping("/update")
    public ResponseEntity<SuccessResponseWithData<PostInfo>> updatePost(@RequestBody EditRequest editRequest) throws Exception {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getId();
        PostInfo updatedPost = postService.updatePost(userId, editRequest);
        return ResponseEntity.ok(new SuccessResponseWithData<>(updatedPost));
    }

    @DeleteMapping
    public ResponseEntity<SuccessResponse> deletePost(@RequestParam("postId") Long postId) throws DeveloperException {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getId();
        postService.deletePost(userId, postId);
        return ResponseEntity.ok(new SuccessResponse("Deleted successfully"));
    }

    @GetMapping("/getAvailablePermission")
    public ResponseEntity<List<String>> getAvailablePermission(){
        return ResponseEntity.ok(postService.getAvailablePermission());
    }

}
