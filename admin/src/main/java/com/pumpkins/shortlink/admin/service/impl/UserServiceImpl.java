package com.pumpkins.shortlink.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pumpkins.shortlink.admin.common.convention.exception.ClientException;
import com.pumpkins.shortlink.admin.common.enums.UserErrorCodeEnum;
import com.pumpkins.shortlink.admin.dao.entity.UserDO;
import com.pumpkins.shortlink.admin.dao.mapper.UserMapper;
import com.pumpkins.shortlink.admin.dto.resp.UserRespDTO;
import com.pumpkins.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/*
 * @author      : pumpkins
 * @date        : 2024/7/11 15:24
 * @description : 用户接口实现层
 * @Copyright   : ...
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;

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
        if (null == userDO) {
            throw new  ClientException(UserErrorCodeEnum.USER_NULL);
        }
        UserRespDTO userRespDTO = new UserRespDTO();
        BeanUtils.copyProperties(userDO, userRespDTO);
        return userRespDTO;
    }

    /**
     * 查询用户名是否存在
     *
     * @param username 用户名
     * @return true表示用户已存在
     */
    @Override
    public Boolean hasUserName(String username) {
        /* LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getUsername, username);
        UserDO userDO = this.baseMapper.selectOne(wrapper);
        return null != userDO; */

        return userRegisterCachePenetrationBloomFilter.contains(username);
    }
}
