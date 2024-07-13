package com.pumpkins.shortlink.admin.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pumpkins.shortlink.admin.common.database.BaseDO;
import lombok.Data;

/*
 * @author      : pumpkins
 * @date        : 2024/7/11 15:07
 * @description : 用户持久层实体
 * @Copyright   : ...
 */
@Data
@TableName("t_user")
public class UserDO extends BaseDO {
    /**
     * id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 注销时间戳
     */
    private Long deletionTime;


}
