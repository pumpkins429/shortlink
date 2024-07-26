package com.pumpkins.shortlink.admin.controller;

import com.pumpkins.shortlink.admin.common.convention.result.Result;
import com.pumpkins.shortlink.admin.common.convention.result.Results;
import com.pumpkins.shortlink.admin.remote.LinkRemoteService;
import com.pumpkins.shortlink.admin.remote.dto.req.LinkMoveToRecycleBInReqDTO;
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
public class RecycleBinController {

    private LinkRemoteService linkRemoteService = new LinkRemoteService() {
    };

    /**
     * 短链移入回收站
     * @param requestParam
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/recycleBin/recycle")
    public Result<Void> moveToRecycleBin(@RequestBody LinkMoveToRecycleBInReqDTO requestParam) {
        linkRemoteService.moveToRecycleBin(requestParam);
        return Results.success();
    }

}
