package com.pumpkins.shortlink.admin.service;/*
 * @author      : pumpkins
 * @date        : 2024/7/13 21:35
 * @description : 短链分组接口层
 * @Copyright   : ...
 */

import com.baomidou.mybatisplus.extension.service.IService;
import com.pumpkins.shortlink.admin.dao.entity.GroupDO;
import com.pumpkins.shortlink.admin.dto.resp.GroupRespDTO;

import java.util.List;

public interface GroupService extends IService<GroupDO> {

    /**
     * 新增分组
     * @param groupName
     */
    void save(String groupName);

    /**
     * 查询当前用户的所有分组
     * @return
     */
    List<GroupRespDTO> listGroup();
}
