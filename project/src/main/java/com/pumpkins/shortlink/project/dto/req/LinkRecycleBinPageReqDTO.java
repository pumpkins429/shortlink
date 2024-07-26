package com.pumpkins.shortlink.project.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pumpkins.shortlink.project.dao.entity.LinkDO;
import lombok.Data;

import java.util.List;

/*
 * @author      : pumpkins
 * @date        : 2024/7/26 15:35
 * @description : ...
 * @Copyright   : ...
 */
@Data
public class LinkRecycleBinPageReqDTO extends Page<LinkDO> {
    /**
     * 分组集合
     */
    private List<String> gids;
}
