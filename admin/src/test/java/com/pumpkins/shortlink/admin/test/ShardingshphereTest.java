package com.pumpkins.shortlink.admin.test;

/*
 * @author      : pumpkins
 * @date        : 2024/7/12 18:59
 * @description : ...
 * @Copyright   : ...
 */
public class ShardingshphereTest {

    public static void main(String[] args) {
        final String sql = "CREATE TABLE `t_link_access_stats_%d`  (\n" +
                "                                               `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',\n" +
                "                                               `full_short_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '完整短链接',\n" +
                "                                               `date` date NULL DEFAULT NULL COMMENT '日期',\n" +
                "                                               `pv` int NULL DEFAULT NULL COMMENT '访问量',\n" +
                "                                               `uv` int NULL DEFAULT NULL COMMENT '独立访问数',\n" +
                "                                               `uip` int NULL DEFAULT NULL COMMENT '独立IP数',\n" +
                "                                               `hour` int NULL DEFAULT NULL COMMENT '小时',\n" +
                "                                               `weekday` int NULL DEFAULT NULL COMMENT '星期',\n" +
                "                                               `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',\n" +
                "                                               `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',\n" +
                "                                               `del_flag` tinyint(1) NULL DEFAULT NULL COMMENT '删除标识：0 未删除 1 已删除',\n" +
                "                                               PRIMARY KEY (`id`) USING BTREE\n" +
                ") ENGINE = InnoDB CHARACTER SET = utf8mb4 ROW_FORMAT = Dynamic;";


        for (int i = 0; i < 8; i++) {
            System.out.printf(sql, i);
            System.out.println();
        }


    }

}
