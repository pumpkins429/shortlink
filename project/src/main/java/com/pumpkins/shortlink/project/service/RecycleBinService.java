package com.pumpkins.shortlink.project.service;/*
 * @author      : pumpkins
 * @date        : 2024/7/25 22:01
 * @description : 回收站接口层
 * @Copyright   : ...
 */

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pumpkins.shortlink.project.dto.req.LinkMoveToRecycleBInReqDTO;
import com.pumpkins.shortlink.project.dto.req.LinkRecycleBinPageReqDTO;
import com.pumpkins.shortlink.project.dto.resp.LinkRecycleBinPageRespDTO;

public interface RecycleBinService {
    /**
     * 将短链移入回收站
     * @return
     */
    void moveToRecycleBin(LinkMoveToRecycleBInReqDTO requestParam);

    /**
     * 分页查询
     * @param requestParam
     * @return
     */
    IPage<LinkRecycleBinPageRespDTO> queryPage(LinkRecycleBinPageReqDTO requestParam);
}
