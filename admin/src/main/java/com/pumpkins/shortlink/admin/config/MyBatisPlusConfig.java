package com.pumpkins.shortlink.admin.config;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/*
 * @author      : pumpkins
 * @date        : 2024/7/14 18:26
 * @description : mp配置类
 * @Copyright   : ...
 */

@Configuration
public class MyBatisPlusConfig {

    /**
     * 配置批量操作模式
     */
    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory, ExecutorType.BATCH);
    }
}
