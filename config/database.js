const { Sequelize } = require('sequelize');
const config = require('./config');

const sequelize = new Sequelize(config.DATABASE_URL, {
  dialect: 'postgres',
  logging: false
});

// 测试数据库连接
const testConnection = async () => {
  try {
    await sequelize.authenticate();
    console.log('数据库连接成功');
  } catch (error) {
    console.error('数据库连接失败:', error);
  }
};

testConnection();

module.exports = sequelize;