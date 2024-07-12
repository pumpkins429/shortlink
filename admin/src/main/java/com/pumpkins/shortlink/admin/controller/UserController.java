package com.pumpkins.shortlink.admin.controller;

/*
 * @author      : pumpkins
 * @date        : 2024/7/8 11:12
 * @description : 用户管理控制层
 * @Copyright   : ...
 */

import cn.hutool.core.bean.BeanUtil;
import com.pumpkins.shortlink.admin.common.convention.result.Result;
import com.pumpkins.shortlink.admin.common.convention.result.Results;
import com.pumpkins.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.pumpkins.shortlink.admin.dto.resp.UserActualRespDTO;
import com.pumpkins.shortlink.admin.dto.resp.UserRespDTO;
import com.pumpkins.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
        return Results.success(userService.getUserByUserName(username));
    }

    /**
     * 根据用户名查询用户信息 （无脱敏
     * @param username
     * @return
     */
    @GetMapping("/api/short-link/v1/actual/user/{username}")
    public Result<UserActualRespDTO> getActualUserByUserName(@PathVariable("username") String username) {
        return Results.success(BeanUtil.toBean(userService.getUserByUserName(username), UserActualRespDTO.class));
    }

    /**
     * 查询用户名是否存在
     * @param username
     * @return
     */
    @GetMapping("/api/short-link/v1/has-username")
    public Result<Boolean> hasUserName(@RequestParam("username") String username) {
        return Results.success(userService.hasUserName(username));
    }

    /**
     * 注册用户
     * @param requestParam 用户注册参数对象
     * @return
     */
    @PostMapping("/api/short-link/v1/user")
    public Result<Void> register(@RequestBody UserRegisterReqDTO requestParam) {
        userService.register(requestParam);
        return Results.success();
    }
}
