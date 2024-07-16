package com.pumpkins.shortlink.admin.controller;

/*
 * @author      : pumpkins
 * @date        : 2024/7/8 11:12
 * @description : 后管用户管理控制层
 * @Copyright   : ...
 */

import cn.hutool.core.bean.BeanUtil;
import com.pumpkins.shortlink.admin.common.convention.result.Result;
import com.pumpkins.shortlink.admin.common.convention.result.Results;
import com.pumpkins.shortlink.admin.dto.req.UserLoginReqDTO;
import com.pumpkins.shortlink.admin.dto.req.UserLogoutReqDTO;
import com.pumpkins.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.pumpkins.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.pumpkins.shortlink.admin.dto.resp.UserActualRespDTO;
import com.pumpkins.shortlink.admin.dto.resp.UserLoginRespDTO;
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
    @GetMapping("/api/short-link/admin/v1/user/{username}")
    public Result<UserRespDTO> getUserByUserName(@PathVariable("username") String username) {
        return Results.success(userService.getUserByUserName(username));
    }

    /**
     * 根据用户名查询用户信息 （无脱敏
     * @param username
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/actual/user/{username}")
    public Result<UserActualRespDTO> getActualUserByUserName(@PathVariable("username") String username) {
        return Results.success(BeanUtil.toBean(userService.getUserByUserName(username), UserActualRespDTO.class));
    }

    /**
     * 查询用户名是否存在
     * @param username
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/has-username")
    public Result<Boolean> hasUserName(@RequestParam("username") String username) {
        return Results.success(userService.hasUserName(username));
    }

    /**
     * 注册用户
     * @param requestParam 用户注册参数对象
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/user")
    public Result<Void> register(@RequestBody UserRegisterReqDTO requestParam) {
        userService.register(requestParam);
        return Results.success();
    }

    /**
     * 更新用户信息
     * @param requestParam 用户更新参数对象
     * @return
     */
    @PutMapping("/api/short-link/admin/v1/user")
    public Result<Void> update(@RequestBody UserUpdateReqDTO requestParam) {
        userService.update(requestParam);
        return Results.success();
    }

    /**
     * 用户登录
     * @param requestParam
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/user/login")
    public Result<UserLoginRespDTO> login(@RequestBody UserLoginReqDTO requestParam) {
        UserLoginRespDTO result = userService.login(requestParam);
        return Results.success(result);
    }

    /**
     * 用户退出登录
     * @param requestParam
     * @return
     */
    @DeleteMapping("/api/short-link/admin/v1/user/logout")
    public Result<Void> logout(@RequestBody UserLogoutReqDTO requestParam) {
        userService.logout(requestParam.getUsername(), requestParam.getToken());
        return Results.success();
    }

}
