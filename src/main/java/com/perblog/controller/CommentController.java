package com.perblog.controller;

import com.perblog.entity.Comment;
import com.perblog.entity.Post;
import com.perblog.service.CommentService;
import com.perblog.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    public CommentController(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    @PostMapping("/add")
    public String addComment(@ModelAttribute Comment comment, @RequestParam Long postId,
                            RedirectAttributes redirectAttributes) {
        try {
            Post post = postService.getPostById(postId);
            if (!post.getPublished()) {
                redirectAttributes.addFlashAttribute("error", "Cannot comment on unpublished post");
                return "redirect:/";
            }
            commentService.createComment(comment, postId);
            redirectAttributes.addFlashAttribute("success", "Comment submitted for review!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error submitting comment: " + e.getMessage());
        }
        return "redirect:/post/" + postId;
    }
}
