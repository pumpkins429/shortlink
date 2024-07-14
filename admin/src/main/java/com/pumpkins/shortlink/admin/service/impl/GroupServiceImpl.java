package com.pumpkins.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pumpkins.shortlink.admin.dao.entity.GroupDO;
import com.pumpkins.shortlink.admin.dao.mapper.GroupMapper;
import com.pumpkins.shortlink.admin.dto.resp.GroupRespDTO;
import com.pumpkins.shortlink.admin.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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
                // TODO 上下文获取用户名
                .username(null)
                .sortOrder(0)
                .build();
        // 数据库给gid和username加了复合索引
        baseMapper.insert(groupDO);
    }

    /**
     * 查询当前用户的所有分组
     *
     * @return
     */
    @Override
    public List<GroupRespDTO> listGroup() {
        LambdaQueryWrapper<GroupDO> wrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getDelFlag, 0)
                // TODO 上下文获取用户名
                .isNull(GroupDO::getUsername)
                .orderByDesc(List.of(GroupDO::getSortOrder, GroupDO::getUpdateTime));
        List<GroupDO> groupList = baseMapper.selectList(wrapper);
        return BeanUtil.copyToList(groupList, GroupRespDTO.class);
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
