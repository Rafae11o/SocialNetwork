package com.socialNetwork.services;

import com.socialNetwork.controllers.PostController;
import com.socialNetwork.dto.user.UserInfo;
import com.socialNetwork.entities.Subscription;
import com.socialNetwork.entities.user.User;
import com.socialNetwork.exceptions.DeveloperException;
import com.socialNetwork.repositories.SubscriptionRepository;
import com.socialNetwork.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    private final String LOG_TAG;
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository, UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        LOG_TAG = SubscriptionService.class.getName();
    }

    /**
     *
     * @param userId - whose subscriptions we want to display
     * @return subscription list
     */
    public List<UserInfo> getSubscriptions(Long userId) {
        List<Subscription> subscriptions = subscriptionRepository.findAllBySubscriberId(userId);
        List<UserInfo> subscriptionsList = new ArrayList<>();
        for(Subscription subscription: subscriptions) {
            subscriptionsList.add(new UserInfo(subscription.getUser()));
        }
        return subscriptionsList;
    }

    /**
     *
     * @param id - whose subscribers we want to display
     * @return list of subscribers
     */
    public List<UserInfo> getSubscribers(Long id) {
        List<Subscription> subscriptions = subscriptionRepository.findAllByUserId(id);
        List<UserInfo> subscribers = new ArrayList<>();
        for(Subscription subscription: subscriptions) {
            subscribers.add(new UserInfo(subscription.getSubscriber()));
        }
        return subscribers;
    }

    /**
     *
     * @param userId - on whom user wants to subscribe
     * @param subscriberId - who wants to subscribe
     * @throws DeveloperException
     */
    @Transactional
    public void subscribe(Long userId, Long subscriberId) throws DeveloperException {
        if(subscriptionRepository.existsBySubscriberIdAndUserId(subscriberId, userId)){
            logger.warn("[subscribe method] Subscription already exist");
            return;
        }
        User user = userRepository.findById(userId).orElseThrow(() -> {
            String info = "User with id: " + userId + " does not exist";
            return new DeveloperException(LOG_TAG + " [subscribe method]", info);
        });
        User subscriber = userRepository.findById(subscriberId).orElseThrow(() -> {
            String info = "Subscriber with id " + subscriberId + " does not exist";
            return new DeveloperException(LOG_TAG + " [subscribe method]", info);
        });;
        Subscription subscription = new Subscription(user, subscriber);
        subscriptionRepository.save(subscription);
    }

    /**
     *
     * @param userId - from whom wants to unsubscribe
     * @param subscriberId - who wants to unsubscribe
     */
    @Transactional
    public void unsubscribe(Long userId, Long subscriberId) {
        if(!subscriptionRepository.existsBySubscriberIdAndUserId(subscriberId, userId)){
            logger.warn("[unsubscribe method] Subscription does not exist");
        }
        subscriptionRepository.deleteBySubscriberIdAndUserId(subscriberId, userId);
    }

}
