package com.pumpkins.shortlink.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pumpkins.shortlink.project.common.convention.result.Result;
import com.pumpkins.shortlink.project.common.convention.result.Results;
import com.pumpkins.shortlink.project.dto.req.LinkCreateReqDTO;
import com.pumpkins.shortlink.project.dto.req.LinkPageReqDTO;
import com.pumpkins.shortlink.project.dto.resp.LinkCreateRespDTO;
import com.pumpkins.shortlink.project.dto.resp.LinkPageRespDTO;
import com.pumpkins.shortlink.project.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/*
 * @author      : pumpkins
 * @date        : 2024/7/16 14:24
 * @description : 短链控制层
 * @Copyright   : ...
 */
@RestController
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;

    /**
     * 创建短链
     * @param requestParam
     * @return
     */
    @PostMapping("/api/short-link/v1/link")
    public Result<LinkCreateRespDTO> saveLink(@RequestBody LinkCreateReqDTO requestParam) {
        return Results.success(linkService.saveLink(requestParam));
    }

    /**
     * 分页查询短链
     * @param requestParam
     * @return
     */
    @GetMapping("/api/short-link/v1/link/page")
    public Result<IPage<LinkPageRespDTO>> queryLinkPage(@RequestBody LinkPageReqDTO requestParam) {
        return Results.success(linkService.queryLinkPage(requestParam));
    }

}
