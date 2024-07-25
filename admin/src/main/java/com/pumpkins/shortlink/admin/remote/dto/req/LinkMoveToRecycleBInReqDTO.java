package com.pumpkins.shortlink.admin.remote.dto.req;

import lombok.Data;
import lombok.experimental.Accessors;

/*
 * @author      : pumpkins
 * @date        : 2024/7/25 22:08
 * @description : 短链移入回收站请求参数
 * @Copyright   : ...
 */
@Data
@Accessors(chain = true)
public class LinkMoveToRecycleBInReqDTO {

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 完整短链接
     */
    private String fullShortUrl;
}
