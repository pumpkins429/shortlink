package com.pumpkins.shortlink.admin.dto.req;

import lombok.Data;

/*
 * @author      : pumpkins
 * @date        : 2024/7/14 16:15
 * @description : 用户修改分组请求参数
 * @Copyright   : ...
 */
@Data
public class GroupUpdateReqDTO {
    /**
     * 分组标识
     */
    private String gid;

    /**
     * 分组名称
     */
    private String name;
}
