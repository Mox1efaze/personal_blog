package com.perblog.repository;

import com.perblog.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    List<Comment> findByPostIdAndApprovedTrueOrderByCreatedAtDesc(Long postId);
    
    Page<Comment> findByPostId(Long postId, Pageable pageable);
    
    Page<Comment> findByApprovedFalse(Pageable pageable);
    
    List<Comment> findTop10ByApprovedTrueOrderByCreatedAtDesc();
}
