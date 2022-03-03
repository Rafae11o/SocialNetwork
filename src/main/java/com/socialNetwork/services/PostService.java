package com.socialNetwork.services;

import com.socialNetwork.controllers.PostController;
import com.socialNetwork.dto.post.CreatePostRequest;
import com.socialNetwork.dto.post.EditRequest;
import com.socialNetwork.dto.post.PostDetails;
import com.socialNetwork.dto.post.PostInfo;
import com.socialNetwork.entities.Subscription;
import com.socialNetwork.entities.post.Comment;
import com.socialNetwork.entities.post.Post;
import com.socialNetwork.entities.post.PostVisionPermission;
import com.socialNetwork.entities.user.User;
import com.socialNetwork.exceptions.DeveloperException;
import com.socialNetwork.exceptions.UserFriendlyException;
import com.socialNetwork.repositories.CommentRepository;
import com.socialNetwork.repositories.PostRepository;
import com.socialNetwork.repositories.SubscriptionRepository;
import com.socialNetwork.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final String LOG_TAG;
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository,
                       SimpMessagingTemplate simpMessagingTemplate, SubscriptionRepository subscriptionRepository){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.subscriptionRepository = subscriptionRepository;
        LOG_TAG = this.getClass().getName();
    }

    /**
     *
     * @param owner_id - owner of post
     * @param postInfo
     * @return created post
     * @throws Exception
     */
    @Transactional
    public PostInfo createPost(Long owner_id, CreatePostRequest postInfo) throws Exception {
        User user = userRepository.findById(owner_id).orElseThrow(() -> {
            String info = "User with id " + owner_id + " not founded";
            return new DeveloperException(LOG_TAG + " [createPost]", info);
        });
        Post post = new Post(user, postInfo);
        post = postRepository.save(post);
        return new PostInfo(post);
    }

    /**
     *
     * @param postId - post id
     * @return post details(comments included)
     * @throws Exception
     */
    public PostDetails findPost(Long postId, Long userId) throws Exception {
        Post post = postRepository.findById(postId).orElseThrow(() -> {
            String info = "Post with id " + postId + " not founded";
            return new DeveloperException(LOG_TAG + " [findPost]", info);
        });
        if(post.getPostVisionPermission() == PostVisionPermission.SUBSCRIBED_USERS &&
                !subscriptionRepository.existsBySubscriberIdAndUserId(userId, post.getOwner().getId())) {
            throw new UserFriendlyException("Permission denied");
        }
        List<Comment> comments = commentRepository.findAllByPostId(postId);
        return new PostDetails(post, comments);
    }

    @Transactional
    public void deletePost(Long userId, Long postId) throws DeveloperException {
        Post post = postRepository.findById(postId).orElseThrow(() -> {
            String info = "Post with id " + postId + " not founded";
            return new DeveloperException(LOG_TAG + " [deletePost method]", info);
        });
        if(!Objects.equals(post.getOwner().getId(), userId)) {
            throw new DeveloperException(LOG_TAG + " [deletePost]", "Illegal request");
        }
        postRepository.deleteById(postId);
    }

    @Transactional
    public PostInfo updatePost(Long userId, EditRequest editRequest) throws Exception {
        Post post = postRepository.findById(editRequest.getId()).orElseThrow(() -> {
            String info = "Post with id " + editRequest.getId() + " not founded";
            return new DeveloperException(LOG_TAG + " [updatePost]", info);
        });
        if(!Objects.equals(post.getOwner().getId(), userId)) {
            throw new DeveloperException(LOG_TAG + " [updatePost]", "Illegal request");
        }
        post.setText(editRequest.getText());
        return new PostInfo(postRepository.save(post));
    }

    /**
     *
     * @return permissions that can be assigned to post
     */
    public List<String> getAvailablePermission() {
        return PostVisionPermission.names();
    }

    /**
     * Notify subscribers
     * @param userId - owner of post
     */
    public void notifySubscribers(Long userId) {
        List<Subscription> subscriptions = subscriptionRepository.findAllByUserId(userId);
        for(Subscription subscription: subscriptions) {
            logger.info("[notifySubscribers] Notifying subscriber with id {}", subscription.getId());
            simpMessagingTemplate.convertAndSend("/posts/" + subscription.getId(), "New post is available");
        }
    }

}
