package com.Thinkee.security.likes;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long>{
    List<Likes> findByUserId(Long id);
    Optional<Likes> findByIdAndPostId(Long id, Long postId);
    List<Likes> findByPostIdAndStatus(Long id, LikeStatus status);
    Optional<Likes> findByIdAndPostIdAndUserId(Long id, Long postId, Long userId);  
    void deleteByPostIdAndUserId(Long postId, Long userId);  
    Optional<Likes> findByPostIdAndUserId(Long postId, Long userId);
}
