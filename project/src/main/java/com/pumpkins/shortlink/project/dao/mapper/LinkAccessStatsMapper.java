package com.pumpkins.shortlink.project.dao.mapper;/*
 * @author      : pumpkins
 * @date        : 2024/7/29 16:14
 * @description : 短链访问监控持久层
 * @Copyright   : ...
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pumpkins.shortlink.project.dao.entity.LinkAccessStatsDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LinkAccessStatsMapper extends BaseMapper<LinkAccessStatsDO> {
    /**
     * 插入监控记录
     * @param obj
     * @return
     */
    boolean insertRecord(LinkAccessStatsDO obj);
}
