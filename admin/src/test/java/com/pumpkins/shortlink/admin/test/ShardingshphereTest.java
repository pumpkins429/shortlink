package com.pumpkins.shortlink.admin.test;

/*
 * @author      : pumpkins
 * @date        : 2024/7/12 18:59
 * @description : ...
 * @Copyright   : ...
 */
public class ShardingshphereTest {

    public static void main(String[] args) {
        final String sql = "CREATE TABLE `t_link_goto_%d`(\n" +
                "      `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',\n" +
                "      `gid` varchar(32) DEFAULT 'default' COMMENT  '分组标识',\n" +
                "      `full_short_url` varchar(128) DEFAULT NULL COMMENT '完整短链接',\n" +
                "      PRIMARY KEY (`id`))\n" +
                "ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;" +
                "\n";


        for (int i = 0; i < 8; i++) {
            System.out.printf(sql, i);
            System.out.println();
        }


    }

}
