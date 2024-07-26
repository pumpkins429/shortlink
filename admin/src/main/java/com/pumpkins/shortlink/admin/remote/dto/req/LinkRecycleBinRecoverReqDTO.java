package com.pumpkins.shortlink.admin.remote.dto.req;

import lombok.Data;

/*
 * @author      : pumpkins
 * @date        : 2024/7/26 18:38
 * @description : 恢复短链请求参数
 * @Copyright   : ...
 */
@Data
public class LinkRecycleBinRecoverReqDTO {

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 短链
     */
    private String fullShortUrl;
}
