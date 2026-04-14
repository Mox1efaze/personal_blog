package com.perblog.repository;

import com.perblog.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    Page<Post> findByPublishedTrue(Pageable pageable);
    
    List<Post> findByPublishedTrueOrderByPublishedAtDesc();
    
    Page<Post> findByCategoryIdAndPublishedTrue(Long categoryId, Pageable pageable);
    
    @Query("SELECT p FROM Post p JOIN p.tags t WHERE t.id = :tagId AND p.published = true")
    Page<Post> findByTagIdAndPublishedTrue(@Param("tagId") Long tagId, Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.published = true AND " +
           "(p.title LIKE %:keyword% OR p.content LIKE %:keyword% OR p.summary LIKE %:keyword%)")
    Page<Post> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    List<Post> findTop5ByPublishedTrueOrderByViewsDesc();
    
    @Query("SELECT p FROM Post p WHERE p.published = true ORDER BY SIZE(p.comments) DESC")
    List<Post> findMostCommentedPosts(Pageable pageable);
}
