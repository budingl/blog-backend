const express = require('express');
const router = express.Router();
const Post = require('../models/Post');
const User = require('../models/User');
const auth = require('../middleware/auth');

// 获取所有文章
router.get('/', async (req, res) => {
  try {
    const posts = await Post.findAll({
      include: [{ model: User, as: 'author', attributes: ['id', 'username'] }],
      order: [['createdAt', 'DESC']]
    });
    res.json(posts);
  } catch (error) {
    res.status(500).json({ message: '服务器错误' });
  }
});

// 获取单个文章
router.get('/:id', async (req, res) => {
  try {
    const post = await Post.findByPk(req.params.id, {
      include: [{ model: User, as: 'author', attributes: ['id', 'username'] }]
    });
    if (!post) {
      return res.status(404).json({ message: '文章不存在' });
    }
    res.json(post);
  } catch (error) {
    res.status(500).json({ message: '服务器错误' });
  }
});

// 创建文章
router.post('/', auth, async (req, res) => {
  try {
    const { title, content } = req.body;
    const post = await Post.create({
      title,
      content,
      authorId: req.user.id
    });
    res.json(post);
  } catch (error) {
    res.status(500).json({ message: '服务器错误' });
  }
});

// 更新文章
router.put('/:id', auth, async (req, res) => {
  try {
    const { title, content } = req.body;
    let post = await Post.findByPk(req.params.id);
    if (!post) {
      return res.status(404).json({ message: '文章不存在' });
    }
    if (post.authorId !== req.user.id) {
      return res.status(401).json({ message: '无权限更新此文章' });
    }
    post = await post.update({ title, content });
    res.json(post);
  } catch (error) {
    res.status(500).json({ message: '服务器错误' });
  }
});

// 删除文章
router.delete('/:id', auth, async (req, res) => {
  try {
    let post = await Post.findByPk(req.params.id);
    if (!post) {
      return res.status(404).json({ message: '文章不存在' });
    }
    if (post.authorId !== req.user.id) {
      return res.status(401).json({ message: '无权限删除此文章' });
    }
    await post.destroy();
    res.json({ message: '文章已删除' });
  } catch (error) {
    res.status(500).json({ message: '服务器错误' });
  }
});

module.exports = router;