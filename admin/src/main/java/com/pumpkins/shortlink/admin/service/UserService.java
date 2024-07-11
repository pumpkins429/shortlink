package com.pumpkins.shortlink.admin.service;
/*
 * @author      : pumpkins
 * @date        : 2024/7/11 15:22
 * @description : 用户接口层
 * @Copyright   : ...
 */

import com.baomidou.mybatisplus.extension.service.IService;
import com.pumpkins.shortlink.admin.dao.entity.UserDO;
import com.pumpkins.shortlink.admin.dto.resp.UserRespDTO;

public interface UserService extends IService<UserDO> {
    /**
     * 更具用户名返回用户信息
     * @param username 用户名
     * @return 用户返回实体
     */
    UserRespDTO getUserByUserName(String username);
}
