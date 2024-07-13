package com.pumpkins.shortlink.admin.dto.req;

import lombok.Data;

/*
 * @author      : pumpkins
 * @date        : 2024/7/13 13:52
 * @description : 用户注册请求对象
 * @Copyright   : ...
 */
@Data
public class UserLoginReqDTO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 验证码 TODO 后期完善
     */
    private String verificationCode;
}
