package com.pumpkins.shortlink.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pumpkins.shortlink.admin.dao.entity.UserDO;
import com.pumpkins.shortlink.admin.dao.mapper.UserMapper;
import com.pumpkins.shortlink.admin.dto.resp.UserRespDTO;
import com.pumpkins.shortlink.admin.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/*
 * @author      : pumpkins
 * @date        : 2024/7/11 15:24
 * @description : 用户接口实现层
 * @Copyright   : ...
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {
    /**
     * 更具用户名返回用户信息
     *
     * @param username 用户名
     * @return 用户返回实体
     */
    @Override
    public UserRespDTO getUserByUserName(String username) {
        LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username);
        UserDO userDO = this.baseMapper.selectOne(wrapper);
        UserRespDTO userRespDTO = new UserRespDTO();
        BeanUtils.copyProperties(userDO, userRespDTO);
        return userRespDTO;
    }
}
