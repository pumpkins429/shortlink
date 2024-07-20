package com.pumpkins.shortlink.admin.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @author      : pumpkins
 * @date        : 2024/7/14 10:53
 * @description : 分组返回对象
 * @Copyright   : ...
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupRespDTO {

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 创建分组用户名
     */
    private String username;

    /**
     * 分组排序
     */
    private Integer sortOrder;

    /**
     * 当前分组的短链数量
     */
    private Integer shortLinkCount;

}
