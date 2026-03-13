require('dotenv').config();

module.exports = {
  DATABASE_URL: process.env.DATABASE_URL || 'postgresql://xiaojiu:xiaojiu@localhost:5432/blog',
  JWT_SECRET: process.env.JWT_SECRET || 'your-secret-key',
  JWT_EXPIRES_IN: process.env.JWT_EXPIRES_IN || '7d'
};