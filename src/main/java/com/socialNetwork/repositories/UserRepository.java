package com.socialNetwork.repositories;

import com.socialNetwork.entities.post.Post;
import com.socialNetwork.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);

    @Query("SELECT p FROM User u INNER JOIN Post p ON u.id=p.owner.id WHERE u.id=?1 AND p.postVisionPermission='EVERYONE'")
    List<Post> findPostsForEveryone(@Param("userId") Long userId);

    @Query("SELECT p FROM User u INNER JOIN Post p ON u.id=p.owner.id WHERE u.id=?1 AND (p.postVisionPermission='AUTHORIZED_USERS' OR p.postVisionPermission='EVERYONE')")
    List<Post> findPostsForAuthorizedUsers(@Param("userId") Long userId);

    @Query("SELECT p FROM User u INNER JOIN Post p ON u.id=p.owner.id WHERE u.id=?1")
    List<Post> findPostsForSubscribedUsers(@Param("userId") Long userId);
}
