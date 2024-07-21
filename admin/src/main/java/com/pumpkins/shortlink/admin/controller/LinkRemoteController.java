package com.pumpkins.shortlink.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pumpkins.shortlink.admin.common.convention.result.Result;
import com.pumpkins.shortlink.admin.remote.LinkRemoteService;
import com.pumpkins.shortlink.admin.remote.dto.req.LinkCreateReqDTO;
import com.pumpkins.shortlink.admin.remote.dto.req.LinkPageReqDTO;
import com.pumpkins.shortlink.admin.remote.dto.req.LinkUpdateGroupReqDTO;
import com.pumpkins.shortlink.admin.remote.dto.req.LinkUpdateReqDTO;
import com.pumpkins.shortlink.admin.remote.dto.resp.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * @author      : pumpkins
 * @date        : 2024/7/16 14:24
 * @description : 短链远程调用控制层
 * @Copyright   : ...
 */
@RestController
public class LinkRemoteController {

    // TODO 后续重构为SpringCloud feign调用
    private LinkRemoteService linkRemoteService = new LinkRemoteService() {};

    /**
     * 创建短链
     * @param requestParam
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/link")
    public Result<LinkCreateRespDTO> saveLink(@RequestBody LinkCreateReqDTO requestParam) throws Exception {
        return linkRemoteService.saveLink(requestParam);
    }

    /**
     * 分页查询短链
     * @param requestParam
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/link/page")
    public Result<IPage<LinkPageRespDTO>> queryLinkPage(@RequestBody LinkPageReqDTO requestParam) throws Exception {
        return linkRemoteService.queryLinkPage(requestParam);
    }

    /**
     * 查询对应分组的短链数量
     * @param gids
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/group-link-count")
    public Result<List<LinkCountQueryRespDTO>> queryLinkCount(@RequestParam List<String> gids) {
        return linkRemoteService.queryLinkCount(gids);
    }

    /**
     * 修改短链接
     * @param requestParam
     * @return
     */
    @PutMapping("/api/short-link/admin/v1/link")
    public Result<LinkUpdateRespDTO> updateLink(@RequestBody LinkUpdateReqDTO requestParam) {
        return linkRemoteService.updateLink(requestParam);
    }

    /**
     * 更新短链分组
     * @param requestParam
     * @return
     */
    @PutMapping("/api/short-link/admin/v1/link-group")
    public Result<LinkUpdateGroupResqDTO> updateLinkGroup(@RequestBody LinkUpdateGroupReqDTO requestParam) {
        return linkRemoteService.updateLinkGroup(requestParam);
    }

}
