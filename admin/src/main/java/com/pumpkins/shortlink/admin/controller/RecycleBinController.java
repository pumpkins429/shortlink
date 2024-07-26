package com.pumpkins.shortlink.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pumpkins.shortlink.admin.common.convention.result.Result;
import com.pumpkins.shortlink.admin.common.convention.result.Results;
import com.pumpkins.shortlink.admin.dto.req.RecycleBinPageReqDTO;
import com.pumpkins.shortlink.admin.dto.resp.RecycleBinPageRespDTO;
import com.pumpkins.shortlink.admin.remote.LinkRemoteService;
import com.pumpkins.shortlink.admin.remote.dto.req.LinkMoveToRecycleBinReqDTO;
import com.pumpkins.shortlink.admin.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/*
 * @author      : pumpkins
 * @date        : 2024/7/26 10:53
 * @description : 回收站控制层
 * @Copyright   : ...
 */
@RestController
@RequiredArgsConstructor
public class RecycleBinController {
    private final RecycleBinService recycleBinService;
    private LinkRemoteService linkRemoteService = new LinkRemoteService() {
    };

    /**
     * 短链移入回收站
     * @param requestParam
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/recycleBin/recycle")
    public Result<Void> moveToRecycleBin(@RequestBody LinkMoveToRecycleBinReqDTO requestParam) {
        linkRemoteService.moveToRecycleBin(requestParam);
        return Results.success();
    }

    /**
     * 查询回收站分页
     * @param requestParam
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/recycleBin/page")
    public Result<IPage<RecycleBinPageRespDTO>> queryPage(@RequestBody RecycleBinPageReqDTO requestParam) {
        return Results.success(recycleBinService.queryPage(requestParam));
    }

}
