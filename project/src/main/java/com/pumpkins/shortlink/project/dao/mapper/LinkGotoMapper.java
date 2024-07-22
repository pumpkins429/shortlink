package com.pumpkins.shortlink.project.dao.mapper;/*
 * @author      : pumpkins
 * @date        : 2024/7/16 14:18
 * @description : 短链路由持久层
 * @Copyright   : ...
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pumpkins.shortlink.project.dao.entity.LinkGotoDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LinkGotoMapper extends BaseMapper<LinkGotoDO> {
}
