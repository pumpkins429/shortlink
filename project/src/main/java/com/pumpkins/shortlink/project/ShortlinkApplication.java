package com.pumpkins.shortlink.project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * @author      : pumpkins
 * @date        : 2024/7/16 14:20
 * @description : 短链项目启动类
 * @Copyright   : ...
 */
@SpringBootApplication
@MapperScan("com.pumpkins.shortlink.project.dao.mapper")
public class ShortlinkApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShortlinkApplication.class, args);
    }
}
