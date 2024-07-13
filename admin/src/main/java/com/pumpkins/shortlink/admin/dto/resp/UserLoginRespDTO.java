package com.pumpkins.shortlink.admin.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @author      : pumpkins
 * @date        : 2024/7/13 13:56
 * @description : 用户请求登录返回对象
 * @Copyright   : ...
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRespDTO {
    String token;
}
