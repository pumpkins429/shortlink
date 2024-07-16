package com.pumpkins.shortlink.project.service;/*
 * @author      : pumpkins
 * @date        : 2024/7/16 14:21
 * @description : 短链接口层
 * @Copyright   : ...
 */

import com.baomidou.mybatisplus.extension.service.IService;
import com.pumpkins.shortlink.project.dao.entity.LinkDO;
import com.pumpkins.shortlink.project.dto.req.LinkCreateReqDTO;
import com.pumpkins.shortlink.project.dto.resp.LinkCreateRespDTO;

public interface LinkService extends IService<LinkDO> {

    /**
     * 创建短链接
     * @param requestParam
     * @return
     */
    LinkCreateRespDTO createLink(LinkCreateReqDTO requestParam);
}
