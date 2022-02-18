package com.socialNetwork.services;

import com.socialNetwork.dto.RegistrationInfo;
import com.socialNetwork.dto.user.UserFeed;
import com.socialNetwork.entities.post.Post;
import com.socialNetwork.entities.user.User;
import com.socialNetwork.exceptions.DeveloperException;
import com.socialNetwork.exceptions.UserFriendlyException;
import com.socialNetwork.repositories.SubscriptionRepository;
import com.socialNetwork.repositories.UserRepository;
import com.socialNetwork.security.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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

    public Optional<User> getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public boolean checkPassword(String password, User user){
        return PasswordEncoder.bCryptPasswordEncoder().matches(password, user.getPassword());
    }

    public void create(RegistrationInfo userInfo) throws UserFriendlyException {
        User user = new User(userInfo);
        userRepository.save(user);
    }

    // Get feed for unauthorized users;
    @Transactional
    public UserFeed getFeed(Long userId) throws DeveloperException {
        User user = userRepository.findById(userId).orElseThrow(() -> new DeveloperException("something"));
        List<Post> posts = userRepository.findPostsForEveryone(userId);
        return new UserFeed(user, posts);
    }

    @Transactional
    public UserFeed getFeed(Long currentUserId, Long userId) throws DeveloperException {
        User user = userRepository.findById(userId).orElseThrow(() -> new DeveloperException("something"));
        List<Post> posts = null;
        if(subscriptionRepository.existsBySubscriberIdAndUserId(currentUserId, userId)) {
            posts = userRepository.findPostsForSubscribedUsers(userId);
        } else {
            posts = userRepository.findPostsForAuthorizedUsers(userId);
        }
        return new UserFeed(user, posts);
    }
}
