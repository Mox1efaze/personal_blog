package com.perblog.service;

import com.perblog.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {
    Post createPost(Post post, Long authorId, Long categoryId, List<Long> tagIds);
    Post updatePost(Long id, Post post, Long categoryId, List<Long> tagIds);
    void deletePost(Long id);
    Post getPostById(Long id);
    Page<Post> getAllPublishedPosts(Pageable pageable);
    Page<Post> getPostsByCategory(Long categoryId, Pageable pageable);
    Page<Post> getPostsByTag(Long tagId, Pageable pageable);
    Page<Post> searchPosts(String keyword, Pageable pageable);
    List<Post> getRecentPosts();
    List<Post> getPopularPosts();
    void incrementViews(Long postId);
}
