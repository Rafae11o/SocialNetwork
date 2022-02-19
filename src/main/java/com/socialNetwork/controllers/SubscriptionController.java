package com.socialNetwork.controllers;

import com.socialNetwork.dto.SubscribeRequest;
import com.socialNetwork.dto.user.UserInfo;
import com.socialNetwork.dto.response.SuccessResponse;
import com.socialNetwork.exceptions.DeveloperException;
import com.socialNetwork.security.CustomUserDetails;
import com.socialNetwork.services.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/subscription")
public class SubscriptionController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/getSubscriptions")
    public ResponseEntity<List<UserInfo>> getSubscriptions() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getId();
        logger.info("[getSubscriptions method] for user with id from token: {}", userId);
        List<UserInfo> subscriptions = subscriptionService.getSubscriptions(userId);
        return ResponseEntity.ok(subscriptions);
    }

    @GetMapping("/getSubscribers")
    public ResponseEntity<List<UserInfo>> getSubscribers() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getId();
        logger.info("[getSubscribers method] for user with id from token: {}", userId);
        List<UserInfo> subscribers = subscriptionService.getSubscribers(userId);
        return ResponseEntity.ok(subscribers);
    }

    @PostMapping("/subscribe")
    public ResponseEntity<SuccessResponse> subscribe(@RequestBody SubscribeRequest subscribeRequest) throws DeveloperException {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Long subscriberId = userDetails.getId();
        logger.info("[subscribe method] RequestBody SubscribeRequest: {}, subscriberId from token: {}", subscribeRequest, subscriberId);
        if(subscribeRequest.getSubscriptionId().equals(subscriberId)) {
            throw new DeveloperException("Subscription controller", "Users id are equal");
        }
        logger.info("Subscribed successfully");
        subscriptionService.subscribe(subscribeRequest.getSubscriptionId(), subscriberId);
        return ResponseEntity.ok(new SuccessResponse("Subscribed successfully"));
    }

    @DeleteMapping("/unsubscribe")
    public ResponseEntity<SuccessResponse> unsubscribe(@RequestParam("subscriptionId") Long subscriptionId) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Long subscriberId = userDetails.getId();
        logger.info("[unsubscribe method] RequestParam subscriptionId: {}, subscriberId from token: {}", subscriptionId, subscriberId);
        subscriptionService.unsubscribe(subscriptionId, subscriberId);
        logger.info("Unsubscribe successfully");
        return ResponseEntity.ok(new SuccessResponse("Unsubscribe successfully"));
    }

}
