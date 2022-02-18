package com.socialNetwork.services;

import com.socialNetwork.dto.user.UserFeed;
import com.socialNetwork.entities.post.Post;
import com.socialNetwork.entities.user.User;
import com.socialNetwork.exceptions.DeveloperException;
import com.socialNetwork.repositories.SubscriptionRepository;
import com.socialNetwork.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    private static final String LOG_TAG = UserService.class.getName();

    @Autowired
    public UserService(UserRepository userRepository, SubscriptionRepository subscriptionRepository){
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    /**
     * Get feed for unauthorized user
     * @param userId - whose feed wants to receive
     * @return user info that is available for unauthorized user
     * @throws DeveloperException if user with userId does not exist
     */
    @Transactional
    public UserFeed getFeed(Long userId) throws DeveloperException {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            String info = "User with id " + userId + " does not exist";
            return new DeveloperException(LOG_TAG + " getFeed", info);
        });
        List<Post> posts = userRepository.findPostsForEveryone(userId);
        return new UserFeed(user, posts);
    }

    /**
     * Get feed for authorized user or subscriber
     * @param currentUserId - user, who wants to get feed
     * @param userId - whose feed wants to receive
     * @return user info that is available for current user
     * @throws DeveloperException if user with userId does not exist
     */
    @Transactional
    public UserFeed getFeed(Long currentUserId, Long userId) throws DeveloperException {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            String info = "User with id " + userId + " does not exist";
            return new DeveloperException(LOG_TAG + " getFeed", info);
        });
        List<Post> posts;
        // If user is subscriber
        if(userId.equals(currentUserId) || subscriptionRepository.existsBySubscriberIdAndUserId(currentUserId, userId)) {
            posts = userRepository.findPostsForSubscribedUsers(userId);
        } else {
            posts = userRepository.findPostsForAuthorizedUsers(userId);
        }
        return new UserFeed(user, posts);
    }
}
