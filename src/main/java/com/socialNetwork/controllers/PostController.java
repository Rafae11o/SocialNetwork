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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/post")
@Slf4j
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

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
        logger.info("[createPost method] RequestBody CreatePostRequest: {}, userId from token: {}", createPostRequest, id);
        PostInfo postInfo = postService.createPost(id, createPostRequest);
        logger.info("[createPost method] Post created successfully: {}", postInfo);
        logger.info("[createPost method] Start to notify subscribers");
        postService.notifySubscribers(id);
        logger.info("[createPost method] All subscribers are notified");
        return ResponseEntity.ok(new SuccessResponseWithData<>(postInfo));
    }

    @MessageMapping("/createPostWithSocket")
    public Message createPostWithSocket(@Payload Message message) throws Exception {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Long id = userDetails.getId();
        logger.info("[createPost] RequestBody CreatePostRequest: {}, userId from token: {}", message.getPayload(), id);
        PostInfo postInfo = postService.createPost(id, (CreatePostRequest) message.getPayload());
        logger.info("[createPost] Post created successfully: {}", postInfo);
        logger.info("[createPost] Start to notify subscribers");
        postService.notifySubscribers(id);
        logger.info("[createPost] All subscribers are notified");
        return message;
    }

    @GetMapping("/getPost")
    public ResponseEntity<SuccessResponseWithData<PostDetails>> getPost(@RequestParam Long id) throws Exception {
        logger.info("[getPost] RequestParam postId: {}", id);
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getId();
        PostDetails post = postService.findPost(id, userId);
        logger.info("Post founded successfully: {}", post);
        return ResponseEntity.ok(new SuccessResponseWithData<>(post));
    }

    @PostMapping("/update")
    public ResponseEntity<SuccessResponseWithData<PostInfo>> updatePost(@RequestBody EditRequest editRequest) throws Exception {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getId();
        logger.info("[updatePost] RequestBody EditRequest: {}, userId from token: {}", editRequest, userId);
        PostInfo updatedPost = postService.updatePost(userId, editRequest);
        logger.info("Post updated successfully: {}", updatedPost);
        return ResponseEntity.ok(new SuccessResponseWithData<>(updatedPost));
    }

    @DeleteMapping
    public ResponseEntity<SuccessResponse> deletePost(@RequestParam("postId") Long postId) throws DeveloperException {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getId();
        logger.info("[updatePost] RequestParam postId: {}, userId from token: {}", postId, userId);
        postService.deletePost(userId, postId);
        logger.info("Post deleted successfully");
        return ResponseEntity.ok(new SuccessResponse("Deleted successfully"));
    }

    @GetMapping("/getAvailablePermission")
    public ResponseEntity<List<String>> getAvailablePermission(){
        return ResponseEntity.ok(postService.getAvailablePermission());
    }

    @GetMapping("/getNewestSubscriptionsPosts")
    public ResponseEntity<SuccessResponseWithData<List<PostInfo>>> getNewestSubscriptionsPosts() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getId();
        return ResponseEntity.ok(new SuccessResponseWithData<>(postService.getNewestSubscriptionsPosts(userId)));
    }

}
