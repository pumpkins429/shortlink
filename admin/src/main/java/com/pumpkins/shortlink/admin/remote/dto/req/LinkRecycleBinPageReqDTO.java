package com.pumpkins.shortlink.admin.remote.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;

/*
 * @author      : pumpkins
 * @date        : 2024/7/26 15:35
 * @description : ...
 * @Copyright   : ...
 */
@Data
public class LinkRecycleBinPageReqDTO extends Page {
    /**
     * 分组集合
     */
    private List<String> gids;
}
