package com.pumpkins.shortlink.project.config;

/*
 * @author      : pumpkins
 * @date        : 2024/7/24 11:09
 * @description : ...
 * @Copyright   : ...
 */

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourcesConfig implements WebMvcConfigurer
{
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        /** 通过url访问项目外的目录图片*/
        registry.addResourceHandler("/static/img/**").addResourceLocations("classpath:/static/img/");
    }

}