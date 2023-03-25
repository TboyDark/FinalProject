package com.Thinkee.security.comments;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
    List<Comments> findByPostId(Long id);    
    Optional<List<Comments>> getCommentsByPostId(Long postId);
    Optional<Comments> getCommentByPostIdAndId(Long postId, Long commentId);
    Optional<Comments> findByIdAndPostIdAndUserId(Long id, Long postId, Long userId);
    void deleteByIdAndPostIdAndUserId(Long id, Long postId, Long userId);
}
