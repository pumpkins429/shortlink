package com.pumpkins.shortlink.admin.service;
/*
 * @author      : pumpkins
 * @date        : 2024/7/11 15:22
 * @description : 用户接口层
 * @Copyright   : ...
 */

import com.baomidou.mybatisplus.extension.service.IService;
import com.pumpkins.shortlink.admin.dao.entity.UserDO;
import com.pumpkins.shortlink.admin.dto.req.UserLoginReqDTO;
import com.pumpkins.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.pumpkins.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.pumpkins.shortlink.admin.dto.resp.UserLoginRespDTO;
import com.pumpkins.shortlink.admin.dto.resp.UserRespDTO;

public interface UserService extends IService<UserDO> {
    /**
     * 根据用户名返回用户信息
     *
     * @param username 用户名
     * @return 用户返回实体
     */
    UserRespDTO getUserByUserName(String username);

    /**
     * 查询用户名是否存在
     *
     * @param username
     * @return
     */
    Boolean hasUserName(String username);

    /**
     * 用户注册
     *
     * @param requestParam 用户注册参数对象
     */
    void register(UserRegisterReqDTO requestParam);

    /**
     * 用户更新
     * @param requestParam
     */
    void update(UserUpdateReqDTO requestParam);

    /**
     * 用户登录
     * @param requestParam
     * @return
     */
     UserLoginRespDTO login(UserLoginReqDTO requestParam);

    /**
     * 检查用户是否登录
     * @param username
     * @param token
     * @return
     */
     Boolean checkLogin(String username, String token);

    /**
     * 用户退出登录
     * @param username
     */
     void logout(String username, String token);
}
