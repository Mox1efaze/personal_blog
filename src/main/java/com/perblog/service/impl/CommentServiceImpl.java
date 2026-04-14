package com.perblog.service.impl;

import com.perblog.entity.Comment;
import com.perblog.entity.Post;
import com.perblog.repository.CommentRepository;
import com.perblog.repository.PostRepository;
import com.perblog.service.CommentService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Comment createComment(Comment comment, Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("Post not found"));

        comment.setPost(post);
        comment.setApproved(false);

        return commentRepository.save(comment);
    }

    @Override
    public Comment approveComment(Long id) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Comment not found"));

        comment.setApproved(true);
        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public Comment getCommentById(Long id) {
        return commentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    @Override
    public List<Comment> getApprovedCommentsByPost(Long postId) {
        return commentRepository.findByPostIdAndApprovedTrueOrderByCreatedAtDesc(postId);
    }

    @Override
    public Page<Comment> getAllComments(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }

    @Override
    public Page<Comment> getPendingComments(Pageable pageable) {
        return commentRepository.findByApprovedFalse(pageable);
    }
}
