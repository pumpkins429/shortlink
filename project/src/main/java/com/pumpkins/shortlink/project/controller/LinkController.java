package com.pumpkins.shortlink.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pumpkins.shortlink.project.common.convention.result.Result;
import com.pumpkins.shortlink.project.common.convention.result.Results;
import com.pumpkins.shortlink.project.dto.req.LinkCreateReqDTO;
import com.pumpkins.shortlink.project.dto.req.LinkPageReqDTO;
import com.pumpkins.shortlink.project.dto.req.LinkUpdateGroupReqDTO;
import com.pumpkins.shortlink.project.dto.req.LinkUpdateReqDTO;
import com.pumpkins.shortlink.project.dto.resp.*;
import com.pumpkins.shortlink.project.service.LinkGotoService;
import com.pumpkins.shortlink.project.service.LinkService;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private final LinkGotoService linkGotoService;

    /**
     * 短链跳转
     * @param shortUri
     * @param request
     * @param response
     */
    @GetMapping("{short-uri}")
    public void restoreUrl(@PathVariable("short-uri") String shortUri, ServletRequest request, ServletResponse response) {
        linkGotoService.restoreUrl(shortUri, request, response);
    }

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

    /**
     * 查询对应分组的短链数量
     * @param gids
     * @return
     */
    @GetMapping("/api/short-link/v1/group-link-count")
    public Result<List<LinkCountQueryRespDTO>> queryLinkCount(@RequestParam List<String> gids) {
        return Results.success(linkService.queryLinkCount(gids));
    }

    /**
     * 修改短链接
     * @param requestParam
     * @return
     */
    @PutMapping("/api/short-link/v1/link")
    public Result<LinkUpdateRespDTO> updateLink(@RequestBody LinkUpdateReqDTO requestParam) {
        return Results.success(linkService.updateLink(requestParam));
    }

    /**
     * 更新短链分组
     * @param requestParam
     * @return
     */
    @PutMapping("/api/short-link/v1/link-group")
    public Result<LinkUpdateGroupResqDTO> updateLinkGroup(@RequestBody LinkUpdateGroupReqDTO requestParam) {
        return Results.success(linkService.updateLinkGroup(requestParam));
    }
}
