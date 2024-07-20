package com.pumpkins.shortlink.admin.remote.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @author      : pumpkins
 * @date        : 2024/7/20 17:04
 * @description : 查询分组下短链数量请求参数
 * @Copyright   : ...
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkCountQueryRespDTO {

    /**
     * 分组标识
     */
    private String gid;
    /**
     * 短链数量
     */
    private Integer shortLinkCount;

}
