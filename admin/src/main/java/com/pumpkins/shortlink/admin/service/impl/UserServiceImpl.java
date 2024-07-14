package com.pumpkins.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pumpkins.shortlink.admin.common.biz.user.UserContext;
import com.pumpkins.shortlink.admin.common.biz.user.UserInfoDTO;
import com.pumpkins.shortlink.admin.common.constant.RedisCacheConstants;
import com.pumpkins.shortlink.admin.common.convention.exception.ClientException;
import com.pumpkins.shortlink.admin.common.enums.UserErrorCodeEnum;
import com.pumpkins.shortlink.admin.dao.entity.UserDO;
import com.pumpkins.shortlink.admin.dao.mapper.UserMapper;
import com.pumpkins.shortlink.admin.dto.req.UserLoginReqDTO;
import com.pumpkins.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.pumpkins.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.pumpkins.shortlink.admin.dto.resp.UserLoginRespDTO;
import com.pumpkins.shortlink.admin.dto.resp.UserRespDTO;
import com.pumpkins.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

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
    private final StringRedisTemplate stringRedisTemplate;

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

    /**
     * 用户登录
     *
     * @param requestParam
     * @return
     */
    @Override
    public UserLoginRespDTO login(UserLoginReqDTO requestParam) {
        // TODO 校验用户信息
        if (!hasUserName(requestParam.getUsername())) {
            throw new ClientException("用户不存在");
        }
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(RedisCacheConstants.USER_LOGIN_TOKEN + requestParam.getUsername()))) {
            throw new ClientException("用户已登录");
        }

        // TODO 防刷
        LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername())
                .eq(UserDO::getPassword, requestParam.getPassword());
        UserDO userDO = baseMapper.selectOne(wrapper);
        if (null == userDO) {
            throw new ClientException("请输入正确的用户名或密码");
        }

        String token = UUID.randomUUID().toString();
        stringRedisTemplate.opsForHash().put(RedisCacheConstants.USER_LOGIN_TOKEN + requestParam.getUsername(), token, JSON.toJSONString(userDO));
        stringRedisTemplate.expire(RedisCacheConstants.USER_LOGIN_TOKEN + requestParam.getUsername(), 30, TimeUnit.DAYS);

        // 保存用户上下文
        UserInfoDTO userInfoDTO = UserInfoDTO.builder()
                .userId(userDO.getId().toString())
                .username(userDO.getUsername())
                .realName(userDO.getRealName())
                .token(token).build();
        UserContext.setUser(userInfoDTO);

        return new UserLoginRespDTO(token);
    }

    /**
     * 检查用户是否登录
     *
     * @param username
     * @return true表示用户已登录
     */
    @Override
    public Boolean checkLogin(String username, String token) {
        return stringRedisTemplate.opsForHash().get(RedisCacheConstants.USER_LOGIN_TOKEN + username, token) != null;
    }

    /**
     * 用户退出登录
     *
     * @param username
     */
    @Override
    public void logout(String username, String token) {
        if (checkLogin(username, token)) {
            stringRedisTemplate.delete(RedisCacheConstants.USER_LOGIN_TOKEN + username);
            return;
        }
        throw new ClientException("用户不存在或未登录");
    }


}
