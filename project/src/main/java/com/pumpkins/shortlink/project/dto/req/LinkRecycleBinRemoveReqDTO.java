package com.pumpkins.shortlink.project.dto.req;

import lombok.Data;

/*
 * @author      : pumpkins
 * @date        : 2024/7/26 18:50
 * @description : 彻底删除短链请求参数
 * @Copyright   : ...
 */
@Data
public class LinkRecycleBinRemoveReqDTO {
    /**
     * 分组标识
     */
    private String gid;

    /**
     * 短链
     */
    private String fullShortUrl;
}
