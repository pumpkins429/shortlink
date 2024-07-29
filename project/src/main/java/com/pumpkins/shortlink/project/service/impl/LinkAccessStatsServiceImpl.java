package com.pumpkins.shortlink.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pumpkins.shortlink.project.dao.entity.LinkAccessStatsDO;
import com.pumpkins.shortlink.project.dao.mapper.LinkAccessStatsMapper;
import com.pumpkins.shortlink.project.service.LinkAccessStatsService;
import jakarta.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/*
 * @author      : pumpkins
 * @date        : 2024/7/29 16:27
 * @description : 短链访问监控接口实现层
 * @Copyright   : ...
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkAccessStatsServiceImpl extends ServiceImpl<LinkAccessStatsMapper, LinkAccessStatsDO> implements LinkAccessStatsService {
    /**
     * 保存监控记录
     *
     * @param shortUri
     * @param request
     * @param linkAccessStatsDO
     * @return
     */
    @Override
    public boolean recordAccessStats(String shortUri, ServletRequest request, LinkAccessStatsDO linkAccessStatsDO) {
        return baseMapper.insertRecord(linkAccessStatsDO);
    }
}
