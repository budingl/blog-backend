# 博客后端API

## 技术栈
- Node.js
- Express
- MongoDB
- JWT
- bcryptjs

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

2. 安装依赖
```bash
npm install
```

3. 配置环境变量
创建 `.env` 文件，添加以下内容：
```
MONGODB_URI=mongodb://localhost:27017/blog
JWT_SECRET=your-secret-key
JWT_EXPIRES_IN=7d
```

4. 启动开发服务器
```bash
npm start
```

## API 端点

### 认证
- `POST /api/auth/register` - 注册新用户
- `POST /api/auth/login` - 用户登录
- `GET /api/auth/me` - 获取当前用户信息（需要认证）

### 文章
- `GET /api/posts` - 获取所有文章
- `GET /api/posts/:id` - 获取单个文章
- `POST /api/posts` - 创建新文章（需要认证）
- `PUT /api/posts/:id` - 更新文章（需要认证）
- `DELETE /api/posts/:id` - 删除文章（需要认证）

### 评论
- `GET /api/comments/post/:postId` - 获取文章的评论
- `POST /api/comments` - 创建新评论（需要认证）
- `DELETE /api/comments/:id` - 删除评论（需要认证）

## 部署

### Heroku
1. 登录 Heroku
```bash
heroku login
```

2. 创建新应用
```bash
heroku create
```

3. 添加 MongoDB Atlas 连接字符串
```bash
heroku config:set MONGODB_URI=<your-mongodb-atlas-connection-string>
heroku config:set JWT_SECRET=<your-secret-key>
heroku config:set JWT_EXPIRES_IN=7d
```

4. 部署代码
```bash
git push heroku main
```

5. 启动应用
```bash
heroku ps:scale web=1
```

### 其他平台
- 确保设置了正确的环境变量
- 确保 MongoDB 连接正常
- 启动应用使用 `npm start` 命令