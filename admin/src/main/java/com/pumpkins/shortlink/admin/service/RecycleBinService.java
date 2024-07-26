package com.pumpkins.shortlink.admin.service;/*
 * @author      : pumpkins
 * @date        : 2024/7/26 12:25
 * @description : 回收站接口层
 * @Copyright   : ...
 */

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pumpkins.shortlink.admin.dto.req.RecycleBinPageReqDTO;
import com.pumpkins.shortlink.admin.dto.resp.RecycleBinPageRespDTO;

public interface RecycleBinService {

    /**
     * 查询回收站分页
     * @param requestParam
     * @return
     */
    IPage<RecycleBinPageRespDTO> queryPage(RecycleBinPageReqDTO requestParam);
}
