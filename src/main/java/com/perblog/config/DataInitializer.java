package com.perblog.config;

import com.perblog.entity.Category;
import com.perblog.entity.Post;
import com.perblog.entity.Tag;
import com.perblog.entity.User;
import com.perblog.repository.CategoryRepository;
import com.perblog.repository.PostRepository;
import com.perblog.repository.TagRepository;
import com.perblog.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, CategoryRepository categoryRepository,
                          TagRepository tagRepository, PostRepository postRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@perblog.com");
            admin.setNickname("管理员");
            admin.setRole("ROLE_ADMIN");
            userRepository.save(admin);
            System.out.println("管理员账号已创建: 用户名=admin, 密码=admin123");
        }

        if (categoryRepository.count() == 0) {
            Category tech = new Category();
            tech.setName("技术");
            tech.setDescription("技术相关的文章");

            Category life = new Category();
            life.setName("生活");
            life.setDescription("生活随想");

            Category travel = new Category();
            travel.setName("旅行");
            travel.setDescription("旅行见闻");

            categoryRepository.saveAll(Arrays.asList(tech, life, travel));
            System.out.println("分类已创建");
        }

        if (tagRepository.count() == 0) {
            Tag java = new Tag();
            java.setName("Java");

            Tag spring = new Tag();
            spring.setName("Spring Boot");

            Tag tutorial = new Tag();
            tutorial.setName("教程");

            Tag programming = new Tag();
            programming.setName("编程");

            tagRepository.saveAll(Arrays.asList(java, spring, tutorial, programming));
            System.out.println("标签已创建");
        }

        if (postRepository.count() == 0) {
            User admin = userRepository.findByUsername("admin").orElseThrow();
            Category tech = categoryRepository.findByName("技术").orElseThrow();
            Tag java = tagRepository.findByName("Java").orElseThrow();
            Tag spring = tagRepository.findByName("Spring Boot").orElseThrow();

            Post post1 = new Post();
            post1.setTitle("欢迎来到个人博客！");
            post1.setSummary("这是你的第一篇博客文章。欢迎来到你的个人博客！");
            post1.setContent("# 欢迎来到个人博客！\n\n这是你的第一篇博客文章。你可以在管理后台编辑这篇文章或者创建新的文章。\n\n## 功能特性\n\n- 撰写和发布博客文章\n- 使用分类和标签组织文章\n- 允许访客发表评论\n- 从管理后台管理所有内容\n\n祝你写作愉快！");
            post1.setAuthor(admin);
            post1.setCategory(tech);
            post1.setTags(Arrays.asList(java, spring));
            post1.setPublished(true);
            post1.setPublishedAt(LocalDateTime.now());
            post1.setViews(10);

            postRepository.save(post1);
            System.out.println("示例文章已创建");
        }
    }
}
