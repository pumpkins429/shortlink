package com.pumpkins.shortlink.admin.controller;

import com.pumpkins.shortlink.admin.common.convention.result.Result;
import com.pumpkins.shortlink.admin.common.convention.result.Results;
import com.pumpkins.shortlink.admin.dto.req.GroupSaveReqDTO;
import com.pumpkins.shortlink.admin.dto.req.GroupSortReqDTO;
import com.pumpkins.shortlink.admin.dto.req.GroupUpdateReqDTO;
import com.pumpkins.shortlink.admin.dto.resp.GroupRespDTO;
import com.pumpkins.shortlink.admin.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public Result<Void> saveGroup(@RequestBody GroupSaveReqDTO requestParam) {
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

    /**
     * 更新用户分组
     * @param requestParam
     * @return
     */
    @PutMapping("/api/short-link/v1/group")
    public Result<Void> updateGroup(@RequestBody GroupUpdateReqDTO requestParam) {
        groupService.update(requestParam);
        return Results.success();
    }

    /**
     * 删除用户分组
     * @param gid 分组标识
     * @return
     */
    @DeleteMapping("/api/short-link/v1/group")
    public Result<Void> deleteGroup(@RequestParam String gid) {
        groupService.remove(gid);
        return Results.success();
    }

    /**
     * 用户分组排序
     * @param requestParam 分组排序请求参数
     * @return
     */
    @PostMapping("/api/short-link/v1/group/sort")
    public Result<Void> sortGroup(@RequestBody List<GroupSortReqDTO> requestParam) {
        groupService.sortGroup(requestParam);
        return Results.success();
    }

}
