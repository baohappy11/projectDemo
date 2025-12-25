-- =============================================
-- 预约表 (reservation)
-- =============================================

DROP TABLE IF EXISTS `reservation`;
CREATE TABLE `reservation` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '预约ID',
  `title` VARCHAR(200) NOT NULL COMMENT '预约标题',
  `content` TEXT COMMENT '预约内容/说明',
  `reservation_time` VARCHAR(50) DEFAULT NULL COMMENT '预约时间（用户期望的讲座/分享时间）',
  `create_time` VARCHAR(50) DEFAULT NULL COMMENT '创建时间（提交预约的时间）',
  `status` VARCHAR(20) DEFAULT '待审核' COMMENT '审核状态：待审核、已通过、已拒绝',
  `user_id` INT(11) DEFAULT NULL COMMENT '用户ID',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '审核备注（管理员审核时填写）',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预约表';

-- 插入测试数据
INSERT INTO `reservation` VALUES
(1, 'Java高级特性分享会', '希望组织一场关于Java 17新特性的技术分享会，包括Record、Sealed Classes等内容', '2025-12-28 14:00:00', '2025-12-24 10:30:00', '已通过', 1, '主题很好，已安排会议室'),
(2, 'Spring Boot最佳实践讲座', '分享Spring Boot项目开发中的最佳实践和常见问题解决方案', '2025-12-29 15:00:00', '2025-12-24 11:20:00', '待审核', 2, NULL),
(3, '数据库性能优化专题', '讲解MySQL索引优化、SQL优化、分库分表等技术', '2025-12-30 10:00:00', '2025-12-24 13:45:00', '已通过', 3, '已协调技术部会议室'),
(4, '前端框架对比分析', '对比React、Vue、Angular三大框架的优劣和适用场景', '2025-12-31 14:30:00', '2025-12-24 14:20:00', '已拒绝', 4, '主题与本月已有分享重复'),
(5, '微服务架构实战分享', '分享微服务架构设计经验和SpringCloud实战案例', '2026-01-03 09:30:00', '2025-12-24 15:10:00', '待审核', 5, NULL);
