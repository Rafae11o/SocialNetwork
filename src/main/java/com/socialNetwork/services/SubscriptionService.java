package com.socialNetwork.services;

import com.socialNetwork.dto.user.UserInfo;
import com.socialNetwork.entities.Subscription;
import com.socialNetwork.entities.user.User;
import com.socialNetwork.exceptions.DeveloperException;
import com.socialNetwork.repositories.SubscriptionRepository;
import com.socialNetwork.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
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

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository, UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
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
            log.warn("Subscription already exist");
            return;
        }
        User user = userRepository.findById(userId).orElseThrow(() -> {
            String info = "UserId: " + userId + ", SubscriberId: " + subscriberId;
            return new DeveloperException("SubscriptionService subscribe", info);
        });
        User subscriber = userRepository.findById(subscriberId).orElseThrow(() -> {
            String info = "UserId: " + userId + ", SubscriptionId: " + subscriberId;
            return new DeveloperException("SubscriptionService subscribe", info);
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
            log.warn("Subscription does not exist");
        }
        subscriptionRepository.deleteBySubscriberIdAndUserId(subscriberId, userId);
    }

}
