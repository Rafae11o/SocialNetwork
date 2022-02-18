package com.socialNetwork.controllers;

import com.socialNetwork.dto.post.comment.CommentInfo;
import com.socialNetwork.dto.post.EditRequest;
import com.socialNetwork.dto.post.comment.CreateCommentRequest;
import com.socialNetwork.dto.response.SuccessResponse;
import com.socialNetwork.dto.response.SuccessResponseWithData;
import com.socialNetwork.exceptions.DeveloperException;
import com.socialNetwork.security.CustomUserDetails;
import com.socialNetwork.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<SuccessResponseWithData<CommentInfo>> createComment(@RequestBody CreateCommentRequest commentInfo) throws DeveloperException {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getId();
        CommentInfo comment = commentService.create(userId, commentInfo);
        return ResponseEntity.ok(new SuccessResponseWithData<>(comment));
    }

    @GetMapping
    public ResponseEntity<SuccessResponseWithData<List<CommentInfo>>> getComments(@RequestParam("postId") Long postId){
        List<CommentInfo> comments = commentService.getComments(postId);
        return ResponseEntity.ok(new SuccessResponseWithData<>(comments));
    }

    @DeleteMapping
    public ResponseEntity<SuccessResponse> deleteComment(@RequestParam("commentId") Long commentId) throws DeveloperException {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getId();
        commentService.delete(userId, commentId);
        return ResponseEntity.ok(new SuccessResponse("Comment deleted successfully"));
    }

    @PostMapping("/edit")
    public ResponseEntity<SuccessResponseWithData<String>> editComment(@RequestBody EditRequest commentEditRequest) throws DeveloperException {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getId();
        String editedText = commentService.edit(userId, commentEditRequest);
        return ResponseEntity.ok(new SuccessResponseWithData<>(editedText));
    }

}
