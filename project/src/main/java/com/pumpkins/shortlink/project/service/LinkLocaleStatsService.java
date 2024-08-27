package com.pumpkins.shortlink.project.service;/*
 * @author      : pumpkins
 * @date        : 2024/7/16 14:21
 * @description : 短链地区监控接口层
 * @Copyright   : ...
 */

import com.baomidou.mybatisplus.extension.service.IService;
import com.pumpkins.shortlink.project.dao.entity.LinkLocaleStatsDO;

public interface LinkLocaleStatsService extends IService<LinkLocaleStatsDO> {

    /**
     * 保存地区监控数据
     * @param linkLocaleStatsDO
     */
    void recordLocaleStats(LinkLocaleStatsDO linkLocaleStatsDO);
}
