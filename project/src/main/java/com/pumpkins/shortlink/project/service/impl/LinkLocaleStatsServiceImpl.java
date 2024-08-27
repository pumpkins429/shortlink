package com.pumpkins.shortlink.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pumpkins.shortlink.project.dao.entity.LinkLocaleStatsDO;
import com.pumpkins.shortlink.project.dao.mapper.LinkLocaleStatsMapper;
import com.pumpkins.shortlink.project.service.LinkLocaleStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/*
 * @author      : pumpkins
 * @date        : 2024/8/15 23:13
 * @description : 短链地区监控接口实现层
 * @Copyright   : ...
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LinkLocaleStatsServiceImpl extends ServiceImpl<LinkLocaleStatsMapper, LinkLocaleStatsDO> implements LinkLocaleStatsService {


    /**
     * 保存地区监控数据
     *
     * @param linkLocaleStatsDO
     */
    @Override
    public void recordLocaleStats(LinkLocaleStatsDO linkLocaleStatsDO) {
        baseMapper.insertRecord(linkLocaleStatsDO);
    }
}
