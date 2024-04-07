CREATE USER 'searchhub'@'%' IDENTIFIED BY 'searchhub';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, ALTER ON `searchhub`.* TO 'searchhub'@'%';
FLUSH PRIVILEGES;

CREATE DATABASE `searchhub`;
USE `searchhub`;

-- 文章表
CREATE TABLE `article` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` VARCHAR(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '标题',
  `content` MEDIUMTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` TINYINT NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`),
  KEY `ix_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章';