package com.pumpkins.shortlink.project.controller;

import com.pumpkins.shortlink.project.common.convention.result.Result;
import com.pumpkins.shortlink.project.common.convention.result.Results;
import com.pumpkins.shortlink.project.dto.req.LinkCreateReqDTO;
import com.pumpkins.shortlink.project.dto.resp.LinkCreateRespDTO;
import com.pumpkins.shortlink.project.service.LinkService;
import lombok.RequiredArgsConstructor;
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
     * 创建duanlianjie
     * @param requestParam
     * @return
     */
    @PostMapping("/api/short-link/v1/link")
    public Result<LinkCreateRespDTO> createLink(@RequestBody LinkCreateReqDTO requestParam) {
        return Results.success(linkService.createLink(requestParam));
    }

}
