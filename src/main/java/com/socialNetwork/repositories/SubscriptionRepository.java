package com.socialNetwork.repositories;

import com.socialNetwork.entities.Subscription;
import com.socialNetwork.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findAllByUserId(Long id);

    List<Subscription> findAllBySubscriberId(Long id);

    void deleteBySubscriberIdAndUserId(Long subscriberId, Long userId);

    boolean existsBySubscriberIdAndUserId(Long subscriberId, Long userId);

}
