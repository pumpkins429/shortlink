package com.pumpkins.shortlink.project.dao.mapper;/*
 * @author      : pumpkins
 * @date        : 2024/7/16 14:18
 * @description : 短链持久层
 * @Copyright   : ...
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pumpkins.shortlink.project.dao.entity.LinkDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LinkMapper extends BaseMapper<LinkDO> {
}
