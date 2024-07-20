package com.pumpkins.shortlink.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pumpkins.shortlink.admin.common.convention.result.Result;
import com.pumpkins.shortlink.admin.remote.LinkRemoteService;
import com.pumpkins.shortlink.admin.remote.dto.req.LinkCreateReqDTO;
import com.pumpkins.shortlink.admin.remote.dto.req.LinkPageReqDTO;
import com.pumpkins.shortlink.admin.remote.dto.resp.LinkCreateRespDTO;
import com.pumpkins.shortlink.admin.remote.dto.resp.LinkPageRespDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}
