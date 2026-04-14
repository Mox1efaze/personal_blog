package com.perblog.controller;

import com.perblog.entity.Category;
import com.perblog.entity.Post;
import com.perblog.entity.Tag;
import com.perblog.service.CategoryService;
import com.perblog.service.PostService;
import com.perblog.service.TagService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    private final PostService postService;
    private final CategoryService categoryService;
    private final TagService tagService;

    public HomeController(PostService postService, CategoryService categoryService, TagService tagService) {
        this.postService = postService;
        this.categoryService = categoryService;
        this.tagService = tagService;
    }

    @GetMapping("/")
    public String home(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "publishedAt"));
        Page<Post> postPage = postService.getAllPublishedPosts(pageable);

        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("tags", tagService.getAllTags());
        model.addAttribute("recentPosts", postService.getRecentPosts());

        return "index";
    }

    @GetMapping("/post/{id}")
    public String postDetail(@PathVariable Long id, Model model) {
        postService.incrementViews(id);
        Post post = postService.getPostById(id);
        model.addAttribute("post", post);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("tags", tagService.getAllTags());
        model.addAttribute("recentPosts", postService.getRecentPosts());
        return "post";
    }

    @GetMapping("/category/{id}")
    public String postsByCategory(@PathVariable Long id, Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "publishedAt"));
        Page<Post> postPage = postService.getPostsByCategory(id, pageable);
        Category category = categoryService.getCategoryById(id);

        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("category", category);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("tags", tagService.getAllTags());
        model.addAttribute("recentPosts", postService.getRecentPosts());

        return "category";
    }

    @GetMapping("/tag/{id}")
    public String postsByTag(@PathVariable Long id, Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "publishedAt"));
        Page<Post> postPage = postService.getPostsByTag(id, pageable);
        Tag tag = tagService.getTagById(id);

        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("tag", tag);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("tags", tagService.getAllTags());
        model.addAttribute("recentPosts", postService.getRecentPosts());

        return "tag";
    }

    @GetMapping("/search")
    public String search(@RequestParam String keyword, Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "publishedAt"));
        Page<Post> postPage = postService.searchPosts(keyword, pageable);

        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("tags", tagService.getAllTags());
        model.addAttribute("recentPosts", postService.getRecentPosts());

        return "search";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
