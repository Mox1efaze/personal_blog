package com.perblog.service;

import com.perblog.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {
    Comment createComment(Comment comment, Long postId);
    Comment approveComment(Long id);
    void deleteComment(Long id);
    Comment getCommentById(Long id);
    List<Comment> getApprovedCommentsByPost(Long postId);
    Page<Comment> getAllComments(Pageable pageable);
    Page<Comment> getPendingComments(Pageable pageable);
}
