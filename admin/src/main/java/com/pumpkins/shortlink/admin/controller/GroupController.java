package com.pumpkins.shortlink.admin.controller;

import com.pumpkins.shortlink.admin.common.convention.result.Result;
import com.pumpkins.shortlink.admin.common.convention.result.Results;
import com.pumpkins.shortlink.admin.dto.req.GroupSaveReqDTO;
import com.pumpkins.shortlink.admin.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/*
 * @author      : pumpkins
 * @date        : 2024/7/13 22:18
 * @description : 后管分组管理控制层
 * @Copyright   : ...
 */
@RestController
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping("/api/short-link/v1/group")
    public Result<Void> save(@RequestBody GroupSaveReqDTO requestParam) {
        groupService.save(requestParam.getGroupName());
        return Results.success();
    }

}
