CREATE TABLE `t_user` (
                          `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                          `username` varchar(256) DEFAULT NULL COMMENT '用户名',
                          `password` varchar(512) DEFAULT NULL COMMENT '密码',
                          `real_name` varchar(256) DEFAULT NULL COMMENT '真实姓名',
                          `phone` varchar(128) DEFAULT NULL COMMENT '手机号',
                          `mail` varchar(512) DEFAULT NULL COMMENT '邮箱',
                          `deletion_time` bigint(20) DEFAULT NULL COMMENT '注销时间戳',
                          `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                          `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                          `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

# 分组表
CREATE TABLE `t_group` (
                           `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                           `gid` varchar(32) DEFAULT NULL COMMENT '分组标识',
                           `name` varchar(64) DEFAULT NULL COMMENT '分组名称',
                           `username` varchar(256) DEFAULT NULL COMMENT '创建分组用户名',
                           `sort_order` int(3) DEFAULT NULL COMMENT '分组排序',
                           `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                           `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                           `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `idx_unique_username_gid` (`gid`,`username`) USING BTREE,
                           UNIQUE KEY `idx_unique_gid` (`gid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;;

# 链接表
CREATE TABLE `t_link` (
                          `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                          `domain` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '域名',
                          `short_uri` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '短链接',
                          `full_short_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '完整短链接 注意这个字段的字符集应该设为utf8bin以便区分大小写',
                          `origin_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '原始链接',
                          `click_num` int(11) DEFAULT 0 COMMENT '点击量',
                          `gid` varchar(32) DEFAULT NULL COMMENT '分组标识',
                          `username` varchar(256) DEFAULT NULL COMMENT '短链所属的用户名',
                          `favicon` varchar(1024) DEFAULT NULL COMMENT '原始链接图标',
                          `enable_status` tinyint(1) DEFAULT NULL COMMENT '启用标识 0：未启用 1：已启用',
                          `created_type` tinyint(1) DEFAULT NULL COMMENT '创建类型 0：控制台 1：接口',
                          `valid_date_type` tinyint(1) DEFAULT NULL COMMENT '有效期类型 0：永久有效 1：用户自定义',
                          `valid_date` datetime DEFAULT NULL COMMENT '有效期',
                          `describe` varchar(1024) DEFAULT NULL COMMENT '描述',
                          `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                          `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                          `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
                          PRIMARY KEY (`id`),
                          UNIQUE KEY `idx_unique_full_short_url` (`full_short_url`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

