package com.pumpkins.shortlink.admin.controller;

/*
 * @author      : pumpkins
 * @date        : 2024/7/8 11:12
 * @description : 用户管理控制层
 * @Copyright   : ...
 */

import com.pumpkins.shortlink.admin.common.result.Result;
import com.pumpkins.shortlink.admin.dto.resp.UserRespDTO;
import com.pumpkins.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    @GetMapping("/api/short-link/v1/user/{username}")
    public Result<UserRespDTO> getUserByUserName(@PathVariable("username") String username) {
        UserRespDTO result = userService.getUserByUserName(username);
        if (null == result) {
            return new Result<UserRespDTO>().setCode("-1").setMessage("用户查询为空");
        } else {
            return new Result<UserRespDTO>().setCode("-1").setData(result);
        }
    }

}
