const express = require('express');
const cors = require('cors');
const config = require('./config/config');
const sequelize = require('./config/database');

// 导入模型
const User = require('./models/User');
const Post = require('./models/Post');
const Comment = require('./models/Comment');

// 导入路由
const authRoutes = require('./routes/auth');
const postRoutes = require('./routes/posts');
const commentRoutes = require('./routes/comments');

const app = express();

// 中间件
app.use(cors());
app.use(express.json());

// 同步数据库模型
sequelize.sync({ alter: true }).then(() => {
  console.log('数据库模型同步成功');
}).catch((error) => {
  console.log('数据库模型同步失败:', error);
});

// 路由
app.use('/api/auth', authRoutes);
app.use('/api/posts', postRoutes);
app.use('/api/comments', commentRoutes);

const PORT = process.env.PORT || 5000;

app.listen(PORT, () => {
  console.log(`服务器运行在端口 ${PORT}`);
});