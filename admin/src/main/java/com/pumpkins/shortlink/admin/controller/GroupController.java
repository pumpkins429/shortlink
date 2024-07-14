package com.pumpkins.shortlink.admin.controller;

import com.pumpkins.shortlink.admin.common.convention.result.Result;
import com.pumpkins.shortlink.admin.common.convention.result.Results;
import com.pumpkins.shortlink.admin.dto.req.GroupSaveReqDTO;
import com.pumpkins.shortlink.admin.dto.resp.GroupRespDTO;
import com.pumpkins.shortlink.admin.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    /**
     * 新增分组
     * @param requestParam
     * @return
     */
    @PostMapping("/api/short-link/v1/group")
    public Result<Void> save(@RequestBody GroupSaveReqDTO requestParam) {
        groupService.save(requestParam.getGroupName());
        return Results.success();
    }

    /**
     * 查询当前用户的所有分组
     * @return
     */
    @GetMapping("/api/short-link/v1/group")
    public Result<List<GroupRespDTO>> listGroup() {
        return Results.success(groupService.listGroup());
    }

}
