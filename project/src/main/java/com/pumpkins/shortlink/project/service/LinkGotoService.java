package com.pumpkins.shortlink.project.service;/*
 * @author      : pumpkins
 * @date        : 2024/7/16 14:21
 * @description : 短链接口层
 * @Copyright   : ...
 */

import com.baomidou.mybatisplus.extension.service.IService;
import com.pumpkins.shortlink.project.dao.entity.LinkGotoDO;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

public interface LinkGotoService extends IService<LinkGotoDO> {

    /**
     * 短链跳转
     * @param shortUri
     * @param request
     * @param response
     */
    void restoreUrl(String shortUri, ServletRequest request, ServletResponse response);
}
