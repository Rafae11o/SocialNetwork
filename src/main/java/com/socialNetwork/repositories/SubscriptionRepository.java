package com.socialNetwork.repositories;

import com.socialNetwork.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findAllByUserId(Long id);

    List<Subscription> findAllBySubscriberId(Long id);

    void deleteBySubscriberIdAndUserId(Long subscriberId, Long userId);

    boolean existsBySubscriberIdAndUserId(Long subscriberId, Long userId);

}
