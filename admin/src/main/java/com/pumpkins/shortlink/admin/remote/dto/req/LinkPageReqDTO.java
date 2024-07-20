package com.pumpkins.shortlink.admin.remote.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/*
 * @author      : pumpkins
 * @date        : 2024/7/19 17:22
 * @description : 短链分页查询请求参数
 * @Copyright   : ...
 */
@Data
public class LinkPageReqDTO extends Page {
    /**
     * 分组表示
     */
    private String gid;

    /**
     * 短链所属用户名
     */
    private String username;


}
