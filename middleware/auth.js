const jwt = require('jsonwebtoken');
const config = require('../config/config');

const auth = (req, res, next) => {
  const token = req.header('Authorization');
  
  if (!token) {
    return res.status(401).json({ message: '未提供认证令牌' });
  }
  
  try {
    const decoded = jwt.verify(token.replace('Bearer ', ''), config.JWT_SECRET);
    req.user = decoded.user;
    next();
  } catch (error) {
    res.status(401).json({ message: '无效的认证令牌' });
  }
};

module.exports = auth;