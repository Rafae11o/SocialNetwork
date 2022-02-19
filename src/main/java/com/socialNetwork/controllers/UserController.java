package com.socialNetwork.controllers;

import com.socialNetwork.dto.response.SuccessResponseWithData;
import com.socialNetwork.dto.user.UserFeed;
import com.socialNetwork.exceptions.DeveloperException;
import com.socialNetwork.security.CustomUserDetails;
import com.socialNetwork.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/api/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getFeed")
    public ResponseEntity<SuccessResponseWithData<UserFeed>> getUserFeed(@RequestParam("userId") Long userId) throws DeveloperException {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentUserId = customUserDetails.getId();
        logger.info("[getUserFeed method] RequestPara userId: {}, currentUserId from token: {}", userId, currentUserId);
        return ResponseEntity.ok(new SuccessResponseWithData<>(userService.getFeed(currentUserId, userId)));
    }

    @GetMapping("/getFeedForUnauthorizedUser")
    public ResponseEntity<SuccessResponseWithData<UserFeed>> getUserFeedForUnauthorizedUser(@RequestParam("userId") Long userId) throws DeveloperException {
        logger.info("[getFeedForUnauthorizedUser method] RequestPara userId: {}", userId);
        return ResponseEntity.ok(new SuccessResponseWithData<>(userService.getFeed(userId)));
    }

}
