const express = require('express');
const router = express.Router();
const User = require('../models/User');
const jwt = require('jsonwebtoken');
const config = require('../config/config');
const auth = require('../middleware/auth');

// 注册
router.post('/register', async (req, res) => {
  try {
    const { username, email, password } = req.body;
    
    // 检查用户名是否已存在
    let user = await User.findOne({ where: { username } });
    if (user) {
      return res.status(400).json({ message: '用户名已存在' });
    }
    
    // 检查邮箱是否已存在
    user = await User.findOne({ where: { email } });
    if (user) {
      return res.status(400).json({ message: '邮箱已存在' });
    }
    
    // 创建新用户
    user = await User.create({ username, email, password });
    
    // 生成JWT令牌
    const payload = { user: { id: user.id } };
    const token = jwt.sign(payload, config.JWT_SECRET, { expiresIn: config.JWT_EXPIRES_IN });
    
    res.json({ token });
  } catch (error) {
    res.status(500).json({ message: '服务器错误' });
  }
});

// 登录
router.post('/login', async (req, res) => {
  try {
    const { email, password } = req.body;
    
    // 检查用户是否存在
    const user = await User.findOne({ where: { email } });
    if (!user) {
      return res.status(400).json({ message: '邮箱或密码错误' });
    }
    
    // 验证密码
    const isMatch = await user.matchPassword(password);
    if (!isMatch) {
      return res.status(400).json({ message: '邮箱或密码错误' });
    }
    
    // 生成JWT令牌
    const payload = { user: { id: user.id } };
    const token = jwt.sign(payload, config.JWT_SECRET, { expiresIn: config.JWT_EXPIRES_IN });
    
    res.json({ token });
  } catch (error) {
    res.status(500).json({ message: '服务器错误' });
  }
});

// 获取当前用户信息
router.get('/me', auth, async (req, res) => {
  try {
    const user = await User.findByPk(req.user.id, { attributes: { exclude: ['password'] } });
    res.json(user);
  } catch (error) {
    res.status(500).json({ message: '服务器错误' });
  }
});

module.exports = router;