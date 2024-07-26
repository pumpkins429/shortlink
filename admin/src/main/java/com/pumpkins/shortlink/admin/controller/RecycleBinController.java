package com.pumpkins.shortlink.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pumpkins.shortlink.admin.common.convention.result.Result;
import com.pumpkins.shortlink.admin.common.convention.result.Results;
import com.pumpkins.shortlink.admin.dto.req.RecycleBinPageReqDTO;
import com.pumpkins.shortlink.admin.dto.resp.RecycleBinPageRespDTO;
import com.pumpkins.shortlink.admin.remote.LinkRemoteService;
import com.pumpkins.shortlink.admin.remote.dto.req.LinkMoveToRecycleBinReqDTO;
import com.pumpkins.shortlink.admin.remote.dto.req.LinkRecycleBinRecoverReqDTO;
import com.pumpkins.shortlink.admin.remote.dto.req.LinkRecycleBinRemoveReqDTO;
import com.pumpkins.shortlink.admin.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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


    /**
     * 恢复短链接
     * @param requestParam
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/recycleBin/recover")
    public Result<Void> recoverFromRecycleBin(@RequestBody LinkRecycleBinRecoverReqDTO requestParam) {
        linkRemoteService.recoverFromRecycleBin(requestParam);
        return Results.success();
    }

    /**
     * 从回收站彻底删除短链接
     * @param requestParam
     * @return
     */
    @DeleteMapping("/api/short-link/admin/v1/recycleBin/remove")
    public Result<Void> removeFromRecycleBin(@RequestBody LinkRecycleBinRemoveReqDTO requestParam) {
        linkRemoteService.removeFromRecycleBin(requestParam);
        return Results.success();
    }
}
