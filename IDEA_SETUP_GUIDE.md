# IDEA 启动配置指南

## 常见问题及解决方案

### 1. 确保项目被识别为Maven项目

**检查方法**：
- 查看项目根目录是否有 `pom.xml` 文件
- `pom.xml` 图标应该是蓝色的Maven图标

**如果不是Maven项目**：
1. 右键点击 `pom.xml`
2. 选择 "Add as Maven Project" 或 "Import as Maven Project"

---

### 2. 检查JDK版本配置

**项目要求**：JDK 17 或更高版本

#### 步骤1：配置Project SDK
1. 打开 `File` → `Project Structure` (Ctrl+Alt+Shift+S)
2. 选择 `Project` 选项卡
3. 设置 `SDK` 为 JDK 17 或更高
4. 设置 `Language level` 为 `17 - Sealed types, pattern matching for switch`

#### 步骤2：配置Module SDK
1. 在 `Project Structure` 中，选择 `Modules`
2. 选择 `perblog` 模块
3. 在 `Dependencies` 选项卡中，确保 `Module SDK` 选择了 JDK 17+

#### 步骤3：配置Maven Runner
1. 打开 `File` → `Settings` (Ctrl+Alt+S)
2. 导航到 `Build, Execution, Deployment` → `Build Tools` → `Maven` → `Runner`
3. 设置 `JRE` 为 JDK 17 或更高

---

### 3. 安装Lombok插件

**Lombok是必需的**，否则会出现编译错误。

#### 安装步骤：
1. 打开 `File` → `Settings` (Ctrl+Alt+S)
2. 导航到 `Plugins`
3. 搜索 `Lombok`
4. 点击 `Install` 安装
5. 重启IDEA

#### 启用注解处理：
1. 打开 `File` → `Settings` (Ctrl+Alt+S)
2. 导航到 `Build, Execution, Deployment` → `Compiler` → `Annotation Processors`
3. 勾选 `Enable annotation processing`
4. 点击 `OK`

---

### 4. 刷新Maven依赖

#### 方法1：使用Maven工具窗口
1. 打开右侧的 `Maven` 工具窗口
2. 点击刷新图标（Reload All Maven Projects）
3. 等待依赖下载完成

#### 方法2：右键菜单
1. 右键点击 `pom.xml`
2. 选择 `Maven` → `Reload Project`

---

### 5. 清理并重新构建项目

#### 步骤1：清理项目
1. 打开 `Build` → `Rebuild Project`
2. 或者使用Maven命令：
   ```
   mvn clean install
   ```

#### 步骤2：重新构建
1. 等待Rebuild完成
2. 检查 `Build` 窗口是否有错误

---

### 6. 配置运行配置

#### 创建Spring Boot运行配置：
1. 点击右上角的运行配置下拉框
2. 选择 `Edit Configurations...`
3. 点击 `+` 号，选择 `Spring Boot`
4. 配置如下：
   - **Name**: `PerBlogApplication`
   - **Main class**: `com.perblog.PerBlogApplication`
   - **Module**: `perblog.main`
   - **JRE**: 选择 JDK 17+
5. 点击 `OK`

#### 直接运行主类：
1. 找到 `src/main/java/com/perblog/PerBlogApplication.java`
2. 右键点击该文件
3. 选择 `Run 'PerBlogApplication.main()'`

---

### 7. 检查常见错误

#### 错误1：找不到符号（Lombok相关）
**症状**：编译时提示找不到getter/setter方法

**解决**：
- 确保已安装Lombok插件
- 确保已启用注解处理
- 重新构建项目

#### 错误2：Java版本不匹配
**症状**：提示 `java: 错误: 不支持的类文件主版本 61`

**解决**：
- 确保所有JDK配置都使用JDK 17+
- 检查Project SDK、Module SDK、Maven Runner的JRE设置

#### 错误3：依赖下载失败
**症状**：Maven依赖红色，无法下载

**解决**：
- 检查网络连接
- 配置Maven镜像源（阿里云）
- 删除 `~/.m2/repository` 下的缓存，重新下载

---

### 8. 配置Maven镜像源（可选，加快下载速度）

编辑 `settings.xml`（通常在 `~/.m2/settings.xml`）：

```xml
<mirrors>
    <mirror>
        <id>aliyunmaven</id>
        <mirrorOf>*</mirrorOf>
        <name>阿里云公共仓库</name>
        <url>https://maven.aliyun.com/repository/public</url>
    </mirror>
</mirrors>
```

或者在IDEA中配置：
1. `File` → `Settings` → `Build, Execution, Deployment` → `Build Tools` → `Maven`
2. 修改 `User settings file` 指向你的settings.xml

---

### 9. 验证项目是否能正常运行

按照以下步骤依次检查：

1. ✅ 项目被识别为Maven项目
2. ✅ JDK版本配置为17+
3. ✅ Lombok插件已安装并启用
4. ✅ Maven依赖已成功导入
5. ✅ 项目可以正常编译（Build → Build Project）
6. ✅ 可以运行 `PerBlogApplication`

---

## 快速检查清单

在IDEA中运行项目前，请确认：

- [ ] JDK 17+ 已安装并配置
- [ ] Lombok插件已安装
- [ ] 注解处理已启用
- [ ] Maven依赖已刷新
- [ ] 项目已成功编译

---

## 如果还有问题

1. 查看IDEA底部的 `Build` 窗口，查看具体错误信息
2. 查看 `Run` 窗口的启动日志
3. 尝试在命令行运行 `mvn spring-boot:run` 验证项目本身是否正常
4. 如果命令行可以运行但IDEA不行，说明是IDEA配置问题

---

## 成功启动后

启动成功后，你会看到类似这样的日志：

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.4)

...
Tomcat started on port 8080 (http)
Started PerBlogApplication in X.XXX seconds
```

然后访问：
- 前台首页: http://localhost:8080
- 后台管理: http://localhost:8080/admin
- 管理员账号: admin / admin123
