package com.socialNetwork.repositories;

import com.socialNetwork.dto.post.PostInfo;
import com.socialNetwork.entities.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByOwnerId(Long id);

    //    SELECT * FROM posts WHERE user_id IN (SELECT user_id FROM subscriptions WHERE subscriber_id = 10);
    @Query("SELECT p FROM Post p WHERE p.owner.id IN (SELECT s.user.id FROM Subscription s WHERE s.subscriber.id=?1) ORDER BY p.created DESC")
    List<Post> findNewestSubscriptionsPosts(Long userId);

}
