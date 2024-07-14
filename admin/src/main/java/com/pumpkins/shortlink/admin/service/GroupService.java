package com.pumpkins.shortlink.admin.service;/*
 * @author      : pumpkins
 * @date        : 2024/7/13 21:35
 * @description : 短链分组接口层
 * @Copyright   : ...
 */

import com.baomidou.mybatisplus.extension.service.IService;
import com.pumpkins.shortlink.admin.dao.entity.GroupDO;
import com.pumpkins.shortlink.admin.dto.req.GroupSortReqDTO;
import com.pumpkins.shortlink.admin.dto.req.GroupUpdateReqDTO;
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

    /**
     * 更新用户分组
     * @param requestParam 更新用户分组请求参数
     */
    void update(GroupUpdateReqDTO requestParam);

    /**
     * 删除分组
     * @param gid 分组标识
     */
    void remove(String gid);

    /**
     * 用户分组排序
     * @param requestParam 分组排序请求参数
     * @return
     */
    void sortGroup(List<GroupSortReqDTO> requestParam);
}
