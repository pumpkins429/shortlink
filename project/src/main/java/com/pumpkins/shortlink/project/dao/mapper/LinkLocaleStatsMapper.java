package com.pumpkins.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pumpkins.shortlink.project.dao.entity.LinkLocaleStatsDO;
import org.apache.ibatis.annotations.Mapper;

/*
 * @author      : pumpkins
 * @date        : 2024/8/15 23:14
 * @description : 短链地区监控持久层
 * @Copyright   : ...
 */
@Mapper
public interface LinkLocaleStatsMapper extends BaseMapper<LinkLocaleStatsDO> {
    /**
     * 保存地区监控数据
     * @param linkLocaleStatsDO
     */
    void insertRecord(LinkLocaleStatsDO linkLocaleStatsDO);
}
