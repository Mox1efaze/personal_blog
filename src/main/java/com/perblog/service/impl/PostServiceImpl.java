package com.perblog.service.impl;

import com.perblog.entity.Category;
import com.perblog.entity.Post;
import com.perblog.entity.Tag;
import com.perblog.entity.User;
import com.perblog.repository.CategoryRepository;
import com.perblog.repository.PostRepository;
import com.perblog.repository.TagRepository;
import com.perblog.repository.UserRepository;
import com.perblog.service.PostService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository,
                          CategoryRepository categoryRepository, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public Post createPost(Post post, Long authorId, Long categoryId, List<Long> tagIds) {
        User author = userRepository.findById(authorId)
            .orElseThrow(() -> new RuntimeException("Author not found"));
        
        Category category = null;
        if (categoryId != null) {
            category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        }

        List<Tag> tags = tagRepository.findAllById(tagIds);

        post.setAuthor(author);
        post.setCategory(category);
        post.setTags(tags);

        if (post.getPublished()) {
            post.setPublishedAt(java.time.LocalDateTime.now());
        }

        return postRepository.save(post);
    }

    @Override
    public Post updatePost(Long id, Post postDetails, Long categoryId, List<Long> tagIds) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setTitle(postDetails.getTitle());
        post.setSummary(postDetails.getSummary());
        post.setContent(postDetails.getContent());
        post.setCoverImage(postDetails.getCoverImage());
        post.setPublished(postDetails.getPublished());

        if (postDetails.getPublished() && post.getPublishedAt() == null) {
            post.setPublishedAt(java.time.LocalDateTime.now());
        }

        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
            post.setCategory(category);
        } else {
            post.setCategory(null);
        }

        if (tagIds != null && !tagIds.isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(tagIds);
            post.setTags(tags);
        } else {
            post.getTags().clear();
        }

        return postRepository.save(post);
    }

    @Override
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    @Override
    public Page<Post> getAllPublishedPosts(Pageable pageable) {
        return postRepository.findByPublishedTrue(pageable);
    }

    @Override
    public Page<Post> getPostsByCategory(Long categoryId, Pageable pageable) {
        return postRepository.findByCategoryIdAndPublishedTrue(categoryId, pageable);
    }

    @Override
    public Page<Post> getPostsByTag(Long tagId, Pageable pageable) {
        return postRepository.findByTagIdAndPublishedTrue(tagId, pageable);
    }

    @Override
    public Page<Post> searchPosts(String keyword, Pageable pageable) {
        return postRepository.searchByKeyword(keyword, pageable);
    }

    @Override
    public List<Post> getRecentPosts() {
        return postRepository.findTop5ByPublishedTrueOrderByViewsDesc();
    }

    @Override
    public List<Post> getPopularPosts() {
        Pageable pageable = PageRequest.of(0, 5);
        return postRepository.findMostCommentedPosts(pageable);
    }

    @Override
    public void incrementViews(Long postId) {
        Post post = getPostById(postId);
        post.setViews(post.getViews() + 1);
        postRepository.save(post);
    }
}
