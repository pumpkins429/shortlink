package com.pumpkins.shortlink.admin.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pumpkins.shortlink.admin.dao.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;

/*
 * @author      : pumpkins
 * @date        : 2024/7/11 15:20
 * @description : 用户持久层
 * @Copyright   : ...
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDO> {
}
