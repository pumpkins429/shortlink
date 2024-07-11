package com.pumpkins.shortlink.admin.dto.resp;

import lombok.Data;

/*
 * @author      : pumpkins
 * @date        : 2024/7/11 15:37
 * @description : 用户返回参数实体
 * @Copyright   : ...
 */
@Data
public class UserRespDTO {

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
    // private String password;

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
}
