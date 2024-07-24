package com.pumpkins.shortlink.admin.controller;

import com.pumpkins.shortlink.admin.common.convention.result.Result;
import com.pumpkins.shortlink.admin.remote.LinkRemoteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
 * @author      : pumpkins
 * @date        : 2024/7/24 14:01
 * @description : 短链标题控制层
 * @Copyright   : ...
 */
@RestController
public class UrlTitleController {

    private final LinkRemoteService linkRemoteService = new LinkRemoteService() {};

    /**
     * 获取url标题
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/tile")
    public Result<String> getUrlTile(@RequestParam("url") String url) {
        return linkRemoteService.getUrlTile(url);
    }
}
