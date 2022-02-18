package com.socialNetwork.controllers;

import com.socialNetwork.dto.SubscribeRequest;
import com.socialNetwork.dto.user.UserInfo;
import com.socialNetwork.dto.response.SuccessResponse;
import com.socialNetwork.exceptions.DeveloperException;
import com.socialNetwork.security.CustomUserDetails;
import com.socialNetwork.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/subscription")
public class SubscriptionController {

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
        List<UserInfo> subscriptions = subscriptionService.getSubscriptions(userId);
        return ResponseEntity.ok(subscriptions);
    }

    @GetMapping("/getSubscribers")
    public ResponseEntity<List<UserInfo>> getSubscribers() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getId();
        List<UserInfo> subscribers = subscriptionService.getSubscribers(userId);
        return ResponseEntity.ok(subscribers);
    }

    @PostMapping("/subscribe")
    public ResponseEntity<SuccessResponse> subscribe(@RequestBody SubscribeRequest subscribeRequest) throws DeveloperException {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Long subscriberId = userDetails.getId();
        System.out.println("subscribeRequest: " + subscribeRequest.getSubscriptionId());
        System.out.println("subscriberId: " + subscriberId);
        if(subscribeRequest.getSubscriptionId().equals(subscriberId)) {
            throw new DeveloperException("Subscription controller", "Users id are equal");
        }
        subscriptionService.subscribe(subscribeRequest.getSubscriptionId(), subscriberId);
        return ResponseEntity.ok(new SuccessResponse("Subscribed successfully"));
    }

    @DeleteMapping("/unsubscribe")
    public ResponseEntity<SuccessResponse> unsubscribe(@RequestParam("subscriptionId") Long subscriptionId) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Long subscriberId = userDetails.getId();
        subscriptionService.unsubscribe(subscriptionId, subscriberId);
        return ResponseEntity.ok(new SuccessResponse("Unsubscribe successfully"));
    }

}
