package com.pumpkins.shortlink.project.service;/*
 * @author      : pumpkins
 * @date        : 2024/7/16 14:21
 * @description : 短链接口层
 * @Copyright   : ...
 */

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pumpkins.shortlink.project.dao.entity.LinkDO;
import com.pumpkins.shortlink.project.dto.req.LinkCreateReqDTO;
import com.pumpkins.shortlink.project.dto.req.LinkPageReqDTO;
import com.pumpkins.shortlink.project.dto.req.LinkUpdateGroupReqDTO;
import com.pumpkins.shortlink.project.dto.req.LinkUpdateReqDTO;
import com.pumpkins.shortlink.project.dto.resp.*;

import java.util.List;

public interface LinkService extends IService<LinkDO> {

    /**
     * 创建短链接
     * @param requestParam
     * @return
     */
    LinkCreateRespDTO saveLink(LinkCreateReqDTO requestParam);


    /**
     * 分页查询短链
     * @param requestParam
     * @return
     */
    IPage<LinkPageRespDTO> queryLinkPage(LinkPageReqDTO requestParam);

    /**
     * 查询对应分组的短链数量
     * @param gids
     * @return
     */
    List<LinkCountQueryRespDTO> queryLinkCount(List<String> gids);

    /**
     * 更改短链信息
     * @param requestParam
     * @return
     */
    LinkUpdateRespDTO updateLink(LinkUpdateReqDTO requestParam);

    /**
     * 更改短链分组
     * @param requestParam
     * @return
     */
    LinkUpdateGroupResqDTO updateLinkGroup(LinkUpdateGroupReqDTO requestParam);
}
