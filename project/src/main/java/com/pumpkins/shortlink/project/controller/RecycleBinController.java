package com.pumpkins.shortlink.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pumpkins.shortlink.project.common.convention.result.Result;
import com.pumpkins.shortlink.project.common.convention.result.Results;
import com.pumpkins.shortlink.project.dto.req.LinkMoveToRecycleBInReqDTO;
import com.pumpkins.shortlink.project.dto.req.LinkRecycleBinPageReqDTO;
import com.pumpkins.shortlink.project.dto.req.LinkRecycleBinRecoverReqDTO;
import com.pumpkins.shortlink.project.dto.req.LinkRecycleBinRemoveReqDTO;
import com.pumpkins.shortlink.project.dto.resp.LinkRecycleBinPageRespDTO;
import com.pumpkins.shortlink.project.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 恢复短链接
     * @param requestParam
     * @return
     */
    @PostMapping("/api/short-link/v1/recycleBin/recover")
    public Result<Void> recoverFromRecycleBin(@RequestBody LinkRecycleBinRecoverReqDTO requestParam) {
        recycleBinService.recoverFromRecycleBin(requestParam);
        return Results.success();
    }

    /**
     * 从回收站彻底删除短链接
     * @param requestParam
     * @return
     */
    @DeleteMapping("/api/short-link/v1/recycleBin/remove")
    public Result<Void> removeFromRecycleBin(@RequestBody LinkRecycleBinRemoveReqDTO requestParam) {
        recycleBinService.removeFromRemoveBin(requestParam);
        return Results.success();
    }
}
