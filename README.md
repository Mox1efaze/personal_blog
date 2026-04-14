# PerBlog - 个人博客网站

一个基于Spring Boot 3的现代化个人博客网站。

## 技术栈

- **后端框架**: Spring Boot 3.2.4
- **数据库**: MySQL 8.0
- **ORM**: Spring Data JPA (Hibernate)
- **模板引擎**: Thymeleaf + Thymeleaf Layout Dialect
- **安全框架**: Spring Security
- **前端**: Bootstrap 5.3 + Bootstrap Icons
- **构建工具**: Maven
- **Java版本**: JDK 17

## 功能特性

### 前台功能
- 首页展示文章列表
- 文章详情页
- 按分类浏览文章
- 按标签浏览文章
- 文章搜索功能
- 评论功能（需审核）
- 响应式设计，支持移动端

### 后台管理功能
- 仪表板（统计概览）
- 文章管理（发布、编辑、删除）
- 分类管理
- 标签管理
- 评论审核和管理
- 用户认证和授权

## 快速开始

### 环境要求

- JDK 17或更高版本
- Maven 3.6+
- MySQL 8.0+

### 1. 数据库配置

创建数据库：

```sql
CREATE DATABASE IF NOT EXISTS perblog 
    DEFAULT CHARACTER SET utf8mb4 
    DEFAULT COLLATE utf8mb4_unicode_ci;
```

或者运行项目中的schema.sql脚本：

```bash
mysql -u root -p < src/main/resources/schema.sql
```

### 2. 修改配置文件

编辑 `src/main/resources/application.properties`，根据你的MySQL配置修改数据库连接信息：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/perblog?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=your_password
```

### 3. 运行项目

使用Maven构建并运行：

```bash
mvn clean install
mvn spring-boot:run
```

或者直接运行主类：

```bash
mvn exec:java -Dexec.mainClass="com.perblog.PerBlogApplication"
```

### 4. 访问应用

- 前台首页: http://localhost:8080
- 后台管理: http://localhost:8080/admin

### 默认账号

项目首次运行时会自动创建一个管理员账号：

- 用户名: `admin`
- 密码: `admin123`

**重要**: 请在生产环境中立即修改默认密码！

## 项目结构

```
perblog/
├── src/
│   ├── main/
│   │   ├── java/com/perblog/
│   │   │   ├── config/          # 配置类
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── DataInitializer.java
│   │   │   ├── controller/      # 控制器
│   │   │   │   ├── HomeController.java
│   │   │   │   ├── AdminController.java
│   │   │   │   └── CommentController.java
│   │   │   ├── entity/          # 实体类
│   │   │   │   ├── User.java
│   │   │   │   ├── Post.java
│   │   │   │   ├── Category.java
│   │   │   │   ├── Tag.java
│   │   │   │   └── Comment.java
│   │   │   ├── repository/      # 数据访问层
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── PostRepository.java
│   │   │   │   ├── CategoryRepository.java
│   │   │   │   ├── TagRepository.java
│   │   │   │   └── CommentRepository.java
│   │   │   ├── service/         # 业务逻辑层
│   │   │   │   ├── PostService.java
│   │   │   │   ├── CategoryService.java
│   │   │   │   ├── TagService.java
│   │   │   │   ├── CommentService.java
│   │   │   │   └── impl/
│   │   │   └── PerBlogApplication.java
│   │   └── resources/
│   │       ├── templates/        # Thymeleaf模板
│   │       │   ├── admin/        # 后台管理页面
│   │       │   ├── layout.html
│   │       │   ├── index.html
│   │       │   ├── post.html
│   │       │   └── ...
│   │       ├── application.properties
│   │       └── schema.sql
└── pom.xml
```

## 主要功能说明

### 文章管理
- 支持Markdown格式的文章内容
- 文章摘要和封面图片
- 草稿和发布状态
- 文章浏览量统计

### 分类和标签
- 灵活的分类管理
- 多标签支持
- 按分类或标签筛选文章

### 评论系统
- 访客评论功能
- 评论审核机制
- 垃圾评论过滤

### 安全特性
- Spring Security认证
- BCrypt密码加密
- 基于角色的访问控制

## 开发指南

### 添加新的依赖

在 `pom.xml` 中添加所需的Maven依赖。

### 修改数据库表结构

实体类使用JPA注解定义，修改实体类后，JPA会自动更新数据库结构（`spring.jpa.hibernate.ddl-auto=update`）。

### 自定义前端样式

前端使用Bootstrap 5，可以在模板文件中添加自定义CSS或修改现有样式。

## 部署

### 打包为JAR文件

```bash
mvn clean package
```

生成的JAR文件位于 `target/perblog-1.0.0.jar`

### 运行JAR文件

```bash
java -jar target/perblog-1.0.0.jar
```

### 使用Docker部署

创建Dockerfile：

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/perblog-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

## 许可证

MIT License

## 贡献

欢迎提交Issue和Pull Request！

## 联系方式

如有问题，请通过以下方式联系：
- 提交Issue
- 发送邮件

---

**注意**: 这是一个演示项目，用于学习和参考。在生产环境使用前，请确保：
1. 修改默认管理员密码
2. 配置HTTPS
3. 做好数据库备份
4. 配置适当的日志和监控
