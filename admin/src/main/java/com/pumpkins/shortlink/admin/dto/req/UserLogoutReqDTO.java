package com.pumpkins.shortlink.admin.dto.req;

import lombok.Data;

/*
 * @author      : pumpkins
 * @date        : 2024/7/13 15:18
 * @description : ...
 * @Copyright   : ...
 */
@Data
public class UserLogoutReqDTO {
    String username;
    String token;
}
