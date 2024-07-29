package com.pumpkins.shortlink.project.service;/*
 * @author      : pumpkins
 * @date        : 2024/7/29 16:26
 * @description : 短链访问监控接口层
 * @Copyright   : ...
 */

import com.baomidou.mybatisplus.extension.service.IService;
import com.pumpkins.shortlink.project.dao.entity.LinkAccessStatsDO;
import jakarta.servlet.ServletRequest;

public interface LinkAccessStatsService extends IService<LinkAccessStatsDO> {

    /**
     * 保存监控记录
     * @param shortUri
     * @param request
     * @param linkAccessStatsDO
     * @return
     */
    boolean recordAccessStats(String shortUri, ServletRequest request, LinkAccessStatsDO linkAccessStatsDO);
}
