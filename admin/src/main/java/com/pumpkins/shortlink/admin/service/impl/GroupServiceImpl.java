package com.pumpkins.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pumpkins.shortlink.admin.common.biz.user.UserContext;
import com.pumpkins.shortlink.admin.common.convention.exception.ClientException;
import com.pumpkins.shortlink.admin.dao.entity.GroupDO;
import com.pumpkins.shortlink.admin.dao.mapper.GroupMapper;
import com.pumpkins.shortlink.admin.dto.req.GroupUpdateReqDTO;
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
                .username(UserContext.getUsername())
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
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .orderByDesc(List.of(GroupDO::getSortOrder, GroupDO::getUpdateTime));
        List<GroupDO> groupList = baseMapper.selectList(wrapper);
        return BeanUtil.copyToList(groupList, GroupRespDTO.class);
    }

    /**
     * 更新用户分组
     *
     * @param requestParam 更新用户分组请求参数
     */
    @Override
    public void update(GroupUpdateReqDTO requestParam) {
        if(!hasGid(requestParam.getGid())) {
            throw new ClientException("分组信息错误");
        }
        LambdaQueryWrapper<GroupDO> wrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getGid, requestParam.getGid());
        baseMapper.update(BeanUtil.toBean(requestParam, GroupDO.class), wrapper);
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
