package com.pumpkins.shortlink.admin.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pumpkins.shortlink.admin.dao.entity.GroupDO;
import com.pumpkins.shortlink.admin.dao.mapper.GroupMapper;
import com.pumpkins.shortlink.admin.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/*
 * @author      : pumpkins
 * @date        : 2024/7/13 21:37
 * @description : 分组接口层实现类
 * @Copyright   : ...
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {
    /**
     * 新增分组
     *
     * @param groupName
     */
    @Override
    public void save(String groupName) {
        // TODO 增加权限验证
        String gid;
        do{
            gid = RandomUtil.randomString(6);
        } while (hasGid(gid));
        GroupDO groupDO = GroupDO.builder()
                .gid(gid)
                .name(groupName)
                .build();
        // 数据库给gid和username加了复合索引
        baseMapper.insert(groupDO);
    }

    /**
     * 查询是否存在gid
     * @param gid
     * @return
     */
    private boolean hasGid(String gid) {
        LambdaQueryWrapper<GroupDO> wrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getGid, gid);
        GroupDO groupDO = baseMapper.selectOne(wrapper);
        return null != groupDO;
    }
}
