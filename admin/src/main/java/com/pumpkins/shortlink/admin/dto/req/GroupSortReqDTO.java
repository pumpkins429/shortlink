package com.pumpkins.shortlink.admin.dto.req;

import lombok.Data;

/*
 * @author      : pumpkins
 * @date        : 2024/7/14 17:40
 * @description : 分组排序请求参数
 * @Copyright   : ...
 */
@Data
public class GroupSortReqDTO {
    /**
     * 分组标识
     */
    private String gid;

    /**
     * 分组排序
     */
    private Integer sortOrder;
}
