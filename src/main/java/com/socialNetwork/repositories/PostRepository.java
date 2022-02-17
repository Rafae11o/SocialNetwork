package com.socialNetwork.repositories;

import com.socialNetwork.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByOwnerId(Long id);
}
