package com.pumpkins.shortlink.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pumpkins.shortlink.admin.common.convention.exception.ServiceException;
import com.pumpkins.shortlink.admin.dto.req.RecycleBinPageReqDTO;
import com.pumpkins.shortlink.admin.dto.resp.GroupRespDTO;
import com.pumpkins.shortlink.admin.dto.resp.RecycleBinPageRespDTO;
import com.pumpkins.shortlink.admin.remote.LinkRemoteService;
import com.pumpkins.shortlink.admin.remote.dto.req.LinkRecycleBinPageReqDTO;
import com.pumpkins.shortlink.admin.remote.dto.resp.LinkRecycleBinPageRespDTO;
import com.pumpkins.shortlink.admin.service.GroupService;
import com.pumpkins.shortlink.admin.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/*
 * @author      : pumpkins
 * @date        : 2024/7/26 12:26
 * @description : 回收站接口实现层
 * @Copyright   : ...
 */
@Service
@RequiredArgsConstructor
public class RecycleBinServiceImpl implements RecycleBinService {

    private final GroupService groupService;
    private final LinkRemoteService linkRemoteService = new LinkRemoteService() {
    };

    /**
     * 查询回收站分页
     *
     * @param requestParam
     * @return
     */
    @Override
    public IPage<RecycleBinPageRespDTO> queryPage(RecycleBinPageReqDTO requestParam) {
        // 查询用户的所有分组
        List<String> gids = groupService.listGroup().stream().map(GroupRespDTO::getGid).toList();
        if (CollUtil.isEmpty(gids)) {
            throw new ServiceException("用户无分组信息");
        }
        LinkRecycleBinPageReqDTO linkRecycleBinPageReqDTO = new LinkRecycleBinPageReqDTO();
        linkRecycleBinPageReqDTO.setGids(gids);
        linkRecycleBinPageReqDTO.setCurrent(requestParam.getCurrent());
        linkRecycleBinPageReqDTO.setSize(requestParam.getSize());

        IPage<LinkRecycleBinPageRespDTO> data = linkRemoteService.queryRecycleBinPage(linkRecycleBinPageReqDTO).getData();
        // System.out.println(data.getClass()); // 打印实际的类名 得到实际为代理对象 无法直接调用convert转换
        // IPage<RecycleBinPageRespDTO> resultPage = data.convert(each -> BeanUtil.toBean(each, RecycleBinPageRespDTO.class));

        // 手动转换分页对象
        IPage<RecycleBinPageRespDTO> resultPage = new Page<>();
        resultPage.setCurrent(data.getCurrent());
        resultPage.setSize(data.getSize());
        resultPage.setTotal(data.getTotal());
        resultPage.setRecords(data.getRecords().stream()
                .map(each -> {
                    String jsonString = JSON.toJSONString(each);
                    return JSON.parseObject(jsonString, RecycleBinPageRespDTO.class);
                })
                .collect(Collectors.toList()));

        return resultPage;
    }
}
