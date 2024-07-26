package com.pumpkins.shortlink.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pumpkins.shortlink.project.common.convention.result.Result;
import com.pumpkins.shortlink.project.common.convention.result.Results;
import com.pumpkins.shortlink.project.dto.req.LinkMoveToRecycleBInReqDTO;
import com.pumpkins.shortlink.project.dto.req.LinkRecycleBinPageReqDTO;
import com.pumpkins.shortlink.project.dto.resp.LinkRecycleBinPageRespDTO;
import com.pumpkins.shortlink.project.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/*
 * @author      : pumpkins
 * @date        : 2024/7/25 21:58
 * @description : 回收站控制层
 * @Copyright   : ...
 */
@RestController
@RequiredArgsConstructor
public class RecycleBinController {

    private final RecycleBinService recycleBinService;

    /**
     * 将短链移入回收站
     * @return
     */
    @PostMapping("/api/short-link/v1/recycleBin/recycle")
    public Result<Void> moveToRecycleBin(@RequestBody LinkMoveToRecycleBInReqDTO requestParam) {
        recycleBinService.moveToRecycleBin(requestParam);
        return Results.success();
    }

    /**
     * 分页查询
     * @param requestParam
     * @return
     */
    @GetMapping("/api/short-link/v1/recycleBin/page")
    public Result<IPage<LinkRecycleBinPageRespDTO>> queryPage(@RequestBody LinkRecycleBinPageReqDTO requestParam) {
        return Results.success(recycleBinService.queryPage(requestParam));
    }
}
