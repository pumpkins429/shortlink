package com.pumpkins.shortlink.project.service;/*
 * @author      : pumpkins
 * @date        : 2024/7/25 22:01
 * @description : 回收站接口层
 * @Copyright   : ...
 */

import com.pumpkins.shortlink.project.dto.req.LinkMoveToRecycleBInReqDTO;

public interface RecycleBinService {
    /**
     * 将短链移入回收站
     * @return
     */
    void moveToRecycleBin(LinkMoveToRecycleBInReqDTO requestParam);
}
