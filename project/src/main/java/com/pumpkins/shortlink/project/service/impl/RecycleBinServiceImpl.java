package com.pumpkins.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pumpkins.shortlink.project.common.biz.user.UserContext;
import com.pumpkins.shortlink.project.common.constant.RedisCacheConstants;
import com.pumpkins.shortlink.project.common.convention.exception.ClientException;
import com.pumpkins.shortlink.project.dao.entity.LinkDO;
import com.pumpkins.shortlink.project.dto.req.LinkMoveToRecycleBInReqDTO;
import com.pumpkins.shortlink.project.dto.req.LinkRecycleBinPageReqDTO;
import com.pumpkins.shortlink.project.dto.resp.LinkRecycleBinPageRespDTO;
import com.pumpkins.shortlink.project.service.LinkService;
import com.pumpkins.shortlink.project.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/*
 * @author      : pumpkins
 * @date        : 2024/7/25 22:02
 * @description : 回收站接口实现层
 * @Copyright   : ...
 */
@Service
@RequiredArgsConstructor
public class RecycleBinServiceImpl implements RecycleBinService {

    private final LinkService linkService;
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 将短链移入回收站
     *
     * @param requestParam
     * @return
     */
    @Override
    public void moveToRecycleBin(LinkMoveToRecycleBInReqDTO requestParam) {
        // TODO 校验参数
        LambdaUpdateWrapper<LinkDO> wrapper = Wrappers.lambdaUpdate(LinkDO.class)
                .eq(LinkDO::getGid, requestParam.getGid())
                .eq(LinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq(LinkDO::getEnableStatus, 1)
                .set(LinkDO::getEnableStatus, 0);
        boolean update = linkService.update(wrapper);
        if (!update) {
            throw new ClientException("短链移入回收站失败！");
        }

        // 清楚缓存中预热的数据
        stringRedisTemplate.delete(RedisCacheConstants.SHORT_LINK_GOTO_KEY + requestParam.getFullShortUrl());
    }

    /**
     * 分页查询
     *
     * @param requestParam
     * @return
     */
    @Override
    public IPage<LinkRecycleBinPageRespDTO> queryPage(LinkRecycleBinPageReqDTO requestParam) {
        LambdaUpdateWrapper<LinkDO> wrapper = Wrappers.lambdaUpdate(LinkDO.class)
                .in(LinkDO::getGid, requestParam.getGids())
                .eq(LinkDO::getUsername, UserContext.getUsername())
                .eq(LinkDO::getEnableStatus, 0)
                .eq(LinkDO::getDelFlag, 0);
        IPage<LinkDO> resultPage = linkService.page(requestParam, wrapper);
        return resultPage.convert(each -> BeanUtil.toBean(each, LinkRecycleBinPageRespDTO.class));
    }


}
