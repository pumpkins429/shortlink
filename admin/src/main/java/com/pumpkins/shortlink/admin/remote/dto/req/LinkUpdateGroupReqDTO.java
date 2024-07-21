package com.pumpkins.shortlink.admin.remote.dto.req;

import lombok.Data;

/*
 * @author      : pumpkins
 * @date        : 2024/7/21 17:58
 * @description : 短链修改分组请求对象
 * @Copyright   : ...
 */
@Data
public class LinkUpdateGroupReqDTO {

    /**
     * 完整短链接 不允许修改
     */
    private String fullShortUrl;

    /**
     * 原来的分组标识
     */
    private String originGid;

    /**
     * 新的分组标识
     */
    private String newGid;
}
