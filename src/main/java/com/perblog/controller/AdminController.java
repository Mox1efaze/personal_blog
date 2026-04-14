package com.perblog.controller;

import com.perblog.entity.Category;
import com.perblog.entity.Comment;
import com.perblog.entity.Post;
import com.perblog.entity.Tag;
import com.perblog.entity.User;
import com.perblog.service.CategoryService;
import com.perblog.service.CommentService;
import com.perblog.service.PostService;
import com.perblog.service.TagService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final PostService postService;
    private final CategoryService categoryService;
    private final TagService tagService;
    private final CommentService commentService;

    public AdminController(PostService postService, CategoryService categoryService,
                          TagService tagService, CommentService commentService) {
        this.postService = postService;
        this.categoryService = categoryService;
        this.tagService = tagService;
        this.commentService = commentService;
    }

    @GetMapping
    public String adminDashboard(Model model) {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> posts = postService.getAllPublishedPosts(pageable);
        Page<Comment> pendingComments = commentService.getPendingComments(PageRequest.of(0, 5));

        model.addAttribute("posts", posts.getContent());
        model.addAttribute("pendingComments", pendingComments.getContent());
        model.addAttribute("totalPosts", posts.getTotalElements());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("tags", tagService.getAllTags());

        return "admin/dashboard";
    }

    @GetMapping("/posts")
    public String managePosts(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> postPage = postService.getAllPublishedPosts(pageable);

        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());

        return "admin/posts";
    }

    @GetMapping("/posts/new")
    public String newPostForm(Model model) {
        model.addAttribute("post", new Post());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("tags", tagService.getAllTags());
        return "admin/post-form";
    }

    @PostMapping("/posts")
    public String createPost(@ModelAttribute Post post, @RequestParam(required = false) Long categoryId,
                            @RequestParam(required = false) List<Long> tagIds,
                            @AuthenticationPrincipal User user,
                            RedirectAttributes redirectAttributes) {
        try {
            postService.createPost(post, user.getId(), categoryId, tagIds);
            redirectAttributes.addFlashAttribute("success", "Post created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating post: " + e.getMessage());
        }
        return "redirect:/admin/posts";
    }

    @GetMapping("/posts/{id}/edit")
    public String editPostForm(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        model.addAttribute("post", post);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("tags", tagService.getAllTags());
        return "admin/post-form";
    }

    @PostMapping("/posts/{id}")
    public String updatePost(@PathVariable Long id, @ModelAttribute Post post,
                            @RequestParam(required = false) Long categoryId,
                            @RequestParam(required = false) List<Long> tagIds,
                            RedirectAttributes redirectAttributes) {
        try {
            postService.updatePost(id, post, categoryId, tagIds);
            redirectAttributes.addFlashAttribute("success", "Post updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating post: " + e.getMessage());
        }
        return "redirect:/admin/posts";
    }

    @PostMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            postService.deletePost(id);
            redirectAttributes.addFlashAttribute("success", "Post deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting post: " + e.getMessage());
        }
        return "redirect:/admin/posts";
    }

    @GetMapping("/categories")
    public String manageCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("category", new Category());
        return "admin/categories";
    }

    @PostMapping("/categories")
    public String createCategory(@ModelAttribute Category category, RedirectAttributes redirectAttributes) {
        try {
            categoryService.createCategory(category);
            redirectAttributes.addFlashAttribute("success", "Category created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating category: " + e.getMessage());
        }
        return "redirect:/admin/categories";
    }

    @PostMapping("/categories/{id}/delete")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("success", "Category deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting category: " + e.getMessage());
        }
        return "redirect:/admin/categories";
    }

    @GetMapping("/tags")
    public String manageTags(Model model) {
        model.addAttribute("tags", tagService.getAllTags());
        model.addAttribute("tag", new Tag());
        return "admin/tags";
    }

    @PostMapping("/tags")
    public String createTag(@ModelAttribute Tag tag, RedirectAttributes redirectAttributes) {
        try {
            tagService.createTag(tag);
            redirectAttributes.addFlashAttribute("success", "Tag created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating tag: " + e.getMessage());
        }
        return "redirect:/admin/tags";
    }

    @PostMapping("/tags/{id}/delete")
    public String deleteTag(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            tagService.deleteTag(id);
            redirectAttributes.addFlashAttribute("success", "Tag deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting tag: " + e.getMessage());
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("/comments")
    public String manageComments(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Comment> commentPage = commentService.getAllComments(pageable);

        model.addAttribute("comments", commentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", commentPage.getTotalPages());

        return "admin/comments";
    }

    @PostMapping("/comments/{id}/approve")
    public String approveComment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            commentService.approveComment(id);
            redirectAttributes.addFlashAttribute("success", "Comment approved!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error approving comment: " + e.getMessage());
        }
        return "redirect:/admin/comments";
    }

    @PostMapping("/comments/{id}/delete")
    public String deleteComment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            commentService.deleteComment(id);
            redirectAttributes.addFlashAttribute("success", "Comment deleted!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting comment: " + e.getMessage());
        }
        return "redirect:/admin/comments";
    }
}
