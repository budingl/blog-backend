# 博客后端API

## 技术栈
- Spring Boot 3.2.0
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT

## 功能
- 用户认证（注册、登录、获取当前用户信息）
- 文章管理（创建、读取、更新、删除）
- 评论管理（创建、读取、删除）

## 安装

1. 克隆项目
```bash
git clone <repository-url>
cd blog-backend
```

2. 构建项目
```bash
mvn clean package -DskipTests
```

3. 配置环境变量
在 `src/main/resources/application.properties` 文件中配置：
```
# Database configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/blog
spring.datasource.username=your-username
spring.datasource.password=your-password

# JWT configuration
jwt.secret=your-secret-key
jwt.expiration=604800000
```

4. 启动应用
```bash
java -jar target/blog-backend-0.0.1-SNAPSHOT.jar
```

## API 端点

### 认证
- `POST /api/auth/register` - 注册新用户
- `POST /api/auth/login` - 用户登录
- `GET /api/auth/me?token=...` - 获取当前用户信息

### 文章
- `GET /api/posts` - 获取所有文章
- `GET /api/posts/{id}` - 获取单个文章
- `POST /api/posts` - 创建新文章（需要认证）
- `PUT /api/posts/{id}` - 更新文章（需要认证）
- `DELETE /api/posts/{id}` - 删除文章（需要认证）

### 评论
- `GET /api/comments/post/{postId}` - 获取文章的评论
- `POST /api/comments` - 创建新评论（需要认证）
- `DELETE /api/comments/{id}` - 删除评论（需要认证）

## 部署

### Docker
1. 构建和运行容器
```bash
docker compose up -d --build
```

2. 检查容器状态
```bash
docker ps
```

### 其他平台
- 确保设置了正确的数据库连接信息
- 确保PostgreSQL数据库可用
- 使用 `java -jar target/blog-backend-0.0.1-SNAPSHOT.jar` 启动应用