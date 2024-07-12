package com.pumpkins.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pumpkins.shortlink.admin.common.constant.RedisCacheConstants;
import com.pumpkins.shortlink.admin.common.convention.exception.ClientException;
import com.pumpkins.shortlink.admin.common.enums.UserErrorCodeEnum;
import com.pumpkins.shortlink.admin.dao.entity.UserDO;
import com.pumpkins.shortlink.admin.dao.mapper.UserMapper;
import com.pumpkins.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.pumpkins.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.pumpkins.shortlink.admin.dto.resp.UserRespDTO;
import com.pumpkins.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

/*
 * @author      : pumpkins
 * @date        : 2024/7/11 15:24
 * @description : 用户接口实现层
 * @Copyright   : ...
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;

    private final RedissonClient redissonClient;

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
        UserDO userDO = baseMapper.selectOne(wrapper);
        if (null == userDO) {
            throw new ClientException(UserErrorCodeEnum.USER_NULL);
        }
        UserRespDTO userRespDTO = new UserRespDTO();
        BeanUtil.copyProperties(userDO, userRespDTO);
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

    /**
     * 用户注册
     *
     * @param requestParam 用户注册参数对象
     */
    @Override
    public void register(UserRegisterReqDTO requestParam) {
        // TODO 校验 以及密码加密保存

        if (hasUserName(requestParam.getUsername())) {
            throw new ClientException(UserErrorCodeEnum.USER_NAME_EXIST);
        }

        String key = RedisCacheConstants.LOCK_USER_REGISTER_KEY + requestParam.getUsername();
        RLock lock = redissonClient.getLock(key);
        try {
            if (lock.tryLock()) {
                int result = baseMapper.insert(BeanUtil.toBean(requestParam, UserDO.class));
                if (result < 1) {
                    throw new ClientException(UserErrorCodeEnum.USER_SAVE_FAIL);
                }
                // 同步到布隆过滤器
                userRegisterCachePenetrationBloomFilter.add(requestParam.getUsername());
                log.info("新建用户成功->{}", requestParam.getUsername());
            } else {
                throw new ClientException(UserErrorCodeEnum.USER_EXIST);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 用户更新
     *
     * @param requestParam
     */
    @Override
    public void update(UserUpdateReqDTO requestParam) {
        // TODO 验证用户名与登录用户名是否相同 后续依赖网关实现
        LambdaUpdateWrapper<UserDO> wrapper = Wrappers.lambdaUpdate(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername());
        baseMapper.update(BeanUtil.toBean(requestParam, UserDO.class), wrapper);
    }
}
