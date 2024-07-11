package com.pumpkins.shortlink.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * @author      : pumpkins
 * @date        : 2024/7/8 10:07
 * @description : 后管启动类
 * @Copyright   : ...
 */
@SpringBootApplication
@MapperScan("com.pumpkins.shortlink.admin.dao.mapper")
public class ShortlinkAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShortlinkAdminApplication.class, args);
    }
}
