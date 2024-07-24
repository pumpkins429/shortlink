package com.pumpkins.shortlink.project.controller;

import com.pumpkins.shortlink.project.common.convention.result.Result;
import com.pumpkins.shortlink.project.common.convention.result.Results;
import com.pumpkins.shortlink.project.service.UrlTitleService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UrlTitleController {

    private final UrlTitleService urlTitleService;

    /**
     * 获取url标题
     * @return
     */
    @GetMapping("/api/short-link/v1/tile")
    public Result<String> getUrlTile(@RequestParam("url") String url) {
        return Results.success(urlTitleService.getUrlTile(url));
    }
}
