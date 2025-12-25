-- =============================================
-- 数据库：web
-- 建表语句和测试数据
-- =============================================

-- 1. 管理员表 (admin)
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  `name` VARCHAR(50) DEFAULT NULL COMMENT '姓名',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '电话',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `role` VARCHAR(20) DEFAULT 'ADMIN' COMMENT '角色',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 插入10条管理员数据（使用正确的头像路径）
INSERT INTO `admin` VALUES
(1, 'admin', '123456', '超级管理员', '13800000001', 'admin@example.com', 'ADMIN', 'http://localhost:9090/files/download/admin-avatar.jpg'),
(2, 'admin2', '123456', '张三', '13800000002', 'zhangsan@example.com', 'ADMIN', 'http://localhost:9090/files/download/admin-avatar.jpg'),
(3, 'admin3', '123456', '李四', '13800000003', 'lisi@example.com', 'ADMIN', 'http://localhost:9090/files/download/admin-avatar.jpg'),
(4, 'manager1', '123456', '王经理', '13800000004', 'wangmgr@example.com', 'ADMIN', 'http://localhost:9090/files/download/admin-avatar.jpg'),
(5, 'manager2', '123456', '赵经理', '13800000005', 'zhaomgr@example.com', 'ADMIN', 'http://localhost:9090/files/download/admin-avatar.jpg'),
(6, 'operator1', '123456', '陈操作员', '13800000006', 'chen@example.com', 'ADMIN', 'http://localhost:9090/files/download/admin-avatar.jpg'),
(7, 'operator2', '123456', '刘操作员', '13800000007', 'liu@example.com', 'ADMIN', 'http://localhost:9090/files/download/admin-avatar.jpg'),
(8, 'supervisor', '123456', '周主管', '13800000008', 'zhou@example.com', 'ADMIN', 'http://localhost:9090/files/download/admin-avatar.jpg'),
(9, 'assistant', '123456', '吴助理', '13800000009', 'wu@example.com', 'ADMIN', 'http://localhost:9090/files/download/admin-avatar.jpg'),
(10, 'support', '123456', '郑客服', '13800000010', 'zheng@example.com', 'ADMIN', 'http://localhost:9090/files/download/admin-avatar.jpg');


-- 2. 用户表 (user)
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  `name` VARCHAR(50) DEFAULT NULL COMMENT '姓名',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '电话',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `role` VARCHAR(20) DEFAULT 'USER' COMMENT '角色',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 插入10条用户数据（使用正确的头像路径）
INSERT INTO `user` VALUES
(1, 'user001', '123456', '张小明', '13900000001', 'zhangxm@qq.com', 'USER', 'http://localhost:9090/files/download/user-avatar.jpg'),
(2, 'user002', '123456', '李小红', '13900000002', 'lixh@qq.com', 'USER', 'http://localhost:9090/files/download/user-avatar.jpg'),
(3, 'user003', '123456', '王小刚', '13900000003', 'wangxg@qq.com', 'USER', 'http://localhost:9090/files/download/user-avatar.jpg'),
(4, 'user004', '123456', '赵小梅', '13900000004', 'zhaoxm@qq.com', 'USER', 'http://localhost:9090/files/download/user-avatar.jpg'),
(5, 'user005', '123456', '陈小华', '13900000005', 'chenxh@qq.com', 'USER', 'http://localhost:9090/files/download/user-avatar.jpg'),
(6, 'user006', '123456', '刘小芳', '13900000006', 'liuxf@qq.com', 'USER', 'http://localhost:9090/files/download/user-avatar.jpg'),
(7, 'user007', '123456', '周小军', '13900000007', 'zhouxj@qq.com', 'USER', 'http://localhost:9090/files/download/user-avatar.jpg'),
(8, 'user008', '123456', '吴小丽', '13900000008', 'wuxl@qq.com', 'USER', 'http://localhost:9090/files/download/user-avatar.jpg'),
(9, 'user009', '123456', '郑小强', '13900000009', 'zhengxq@qq.com', 'USER', 'http://localhost:9090/files/download/user-avatar.jpg'),
(10, 'user010', '123456', '孙小燕', '13900000010', 'sunxy@qq.com', 'USER', 'http://localhost:9090/files/download/user-avatar.jpg');


-- 3. 分类表 (category)
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `title` VARCHAR(100) NOT NULL COMMENT '分类名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分类表';

-- 插入10条分类数据
INSERT INTO `category` VALUES
(1, 'Java编程'),
(2, 'Python开发'),
(3, '前端技术'),
(4, '数据库'),
(5, '人工智能'),
(6, '云计算'),
(7, '网络安全'),
(8, '移动开发'),
(9, '运维技术'),
(10, '架构设计');


-- 4. 公告表 (notice)
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `title` VARCHAR(200) NOT NULL COMMENT '公告标题',
  `content` TEXT COMMENT '公告内容',
  `time` VARCHAR(50) DEFAULT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

-- 插入10条公告数据
INSERT INTO `notice` VALUES
(1, '系统维护通知', '系统将于本周六凌晨2点进行例行维护，预计维护时间2小时，请各位用户提前做好准备。', '2025-12-20 10:00:00'),
(2, '新功能上线', '知识库新增分类功能，用户可以按照不同分类浏览知识内容，欢迎体验！', '2025-12-21 09:30:00'),
(3, '节假日安排', '元旦假期期间，客服服务时间调整为9:00-18:00，敬请谅解。', '2025-12-22 14:20:00'),
(4, '安全提醒', '近期发现部分账号存在弱密码，请及时修改密码，确保账号安全。', '2025-12-22 16:45:00'),
(5, '知识分享活动', '本月优秀知识分享者将获得精美礼品一份，欢迎大家积极投稿！', '2025-12-23 08:15:00'),
(6, '用户协议更新', '用户协议已更新，请仔细阅读新版协议内容。', '2025-12-23 11:00:00'),
(7, '平台升级公告', '平台将在下周进行技术升级，升级后页面加载速度将提升50%。', '2025-12-23 15:30:00'),
(8, '在线人数突破', '平台在线用户数突破10000人，感谢大家的支持！', '2025-12-24 09:00:00'),
(9, '新增积分系统', '平台将于下月上线积分系统，分享知识可获得积分奖励。', '2025-12-24 10:20:00'),
(10, '移动端App上线', '移动端App正式上线，支持iOS和Android系统，欢迎下载使用。', '2025-12-24 13:45:00');


-- 5. 知识库表 (knowledge)
DROP TABLE IF EXISTS `knowledge`;
CREATE TABLE `knowledge` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '知识ID',
  `img` VARCHAR(255) DEFAULT NULL COMMENT '封面图片',
  `title` VARCHAR(200) NOT NULL COMMENT '标题',
  `content` TEXT COMMENT '内容',
  `time` VARCHAR(50) DEFAULT NULL COMMENT '发布时间',
  `category_id` INT(11) DEFAULT NULL COMMENT '分类ID',
  `user_id` INT(11) DEFAULT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识库表';

-- 插入10条知识数据（使用car目录的图片）
INSERT INTO `knowledge` VALUES
(1, 'http://localhost:9090/files/download/imge1.jpg', 'Java基础入门教程', 'Java是一种面向对象的编程语言，具有跨平台、安全性高等特点...', '2025-12-20 10:30:00', 1, 1),
(2, 'http://localhost:9090/files/download/imge2.jpg', 'Python数据分析入门', 'Python是一种简洁易学的编程语言，在数据分析领域应用广泛...', '2025-12-20 14:20:00', 2, 2),
(3, 'http://localhost:9090/files/download/image3.jpg', 'Vue3新特性详解', 'Vue3带来了Composition API等重要特性，大幅提升开发体验...', '2025-12-21 09:15:00', 3, 3),
(4, 'http://localhost:9090/files/download/image4.jpg', 'MySQL性能优化技巧', '数据库性能优化是后端开发的重要技能，本文介绍常用优化方法...', '2025-12-21 11:45:00', 4, 4),
(5, 'http://localhost:9090/files/download/image5.jpg', '深度学习框架对比', '目前主流的深度学习框架有TensorFlow、PyTorch等...', '2025-12-22 08:30:00', 5, 5),
(6, 'http://localhost:9090/files/download/image6.jpg', 'Docker容器化实践', 'Docker容器技术可以简化应用部署，提高资源利用率...', '2025-12-22 15:00:00', 6, 6),
(7, 'http://localhost:9090/files/download/image7.jpg', 'Web安全防护指南', 'Web安全包括XSS、CSRF、SQL注入等多种攻击方式的防护...', '2025-12-23 10:20:00', 7, 7),
(8, 'http://localhost:9090/files/download/image8.jpg', 'Flutter跨平台开发', 'Flutter可以使用一套代码构建iOS和Android应用...', '2025-12-23 14:50:00', 8, 8),
(9, 'http://localhost:9090/files/download/image9.jpg', 'Kubernetes集群搭建', 'Kubernetes是当前最流行的容器编排平台...', '2025-12-24 09:30:00', 9, 9),
(10, 'http://localhost:9090/files/download/image11.png', '微服务架构设计', '微服务架构将大型应用拆分为多个小型服务，提高系统可维护性...', '2025-12-24 11:15:00', 10, 10);
