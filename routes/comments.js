const express = require('express');
const router = express.Router();
const Comment = require('../models/Comment');
const User = require('../models/User');
const auth = require('../middleware/auth');

// 获取文章的评论
router.get('/post/:postId', async (req, res) => {
  try {
    const comments = await Comment.findAll({
      where: { postId: req.params.postId },
      include: [{ model: User, as: 'author', attributes: ['id', 'username'] }],
      order: [['createdAt', 'DESC']]
    });
    res.json(comments);
  } catch (error) {
    res.status(500).json({ message: '服务器错误' });
  }
});

// 创建评论
router.post('/', auth, async (req, res) => {
  try {
    const { content, post } = req.body;
    const comment = await Comment.create({
      content,
      postId: post,
      authorId: req.user.id
    });
    res.json(comment);
  } catch (error) {
    res.status(500).json({ message: '服务器错误' });
  }
});

// 删除评论
router.delete('/:id', auth, async (req, res) => {
  try {
    let comment = await Comment.findByPk(req.params.id);
    if (!comment) {
      return res.status(404).json({ message: '评论不存在' });
    }
    if (comment.authorId !== req.user.id) {
      return res.status(401).json({ message: '无权限删除此评论' });
    }
    await comment.destroy();
    res.json({ message: '评论已删除' });
  } catch (error) {
    res.status(500).json({ message: '服务器错误' });
  }
});

module.exports = router;