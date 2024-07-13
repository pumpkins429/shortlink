package com.pumpkins.shortlink.admin.service;/*
 * @author      : pumpkins
 * @date        : 2024/7/13 21:35
 * @description : 短链分组接口层
 * @Copyright   : ...
 */

import com.baomidou.mybatisplus.extension.service.IService;
import com.pumpkins.shortlink.admin.dao.entity.GroupDO;

public interface GroupService extends IService<GroupDO> {

    /**
     * 新增分组
     * @param groupName
     */
    void save(String groupName);
}
