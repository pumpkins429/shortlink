package com.pumpkins.shortlink.project.service.impl;

import cn.hutool.core.text.StrBuilder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pumpkins.shortlink.project.dao.entity.LinkDO;
import com.pumpkins.shortlink.project.dao.entity.LinkGotoDO;
import com.pumpkins.shortlink.project.dao.mapper.LinkGotoMapper;
import com.pumpkins.shortlink.project.service.LinkGotoService;
import com.pumpkins.shortlink.project.service.LinkService;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.stereotype.Service;

/*
 * @author      : pumpkins
 * @date        : 2024/7/16 14:23
 * @description : 短链路由接口实现类
 * @Copyright   : ...
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LinkGotoServiceImpl extends ServiceImpl<LinkGotoMapper, LinkGotoDO> implements LinkGotoService {

    private final RBloomFilter<String> bloomFilter;
    private final LinkService linkService;

    /**
     * 短链跳转
     *
     * @param shortUri 短链后缀
     * @param request
     * @param response
     */
    @SneakyThrows
    @Override
    public void restoreUrl(String shortUri, ServletRequest request, ServletResponse response) {
        String serverName = request.getServerName();
        // TODO 后续实现域名管理统一设置
        String fullShortUrl = StrBuilder.create(serverName)
                .append("/")
                .append(shortUri)
                .toString();

        // 查询路由表
        LambdaQueryWrapper<LinkGotoDO> linkGotoDOLambdaQueryWrapper = Wrappers.lambdaQuery(LinkGotoDO.class)
                .eq(LinkGotoDO::getFullShortUrl, fullShortUrl);
        LinkGotoDO linkGotoDO = baseMapper.selectOne(linkGotoDOLambdaQueryWrapper);
        if (linkGotoDO == null) {
            // TODO 此处应该进行风控
            return;
        }
        LambdaQueryWrapper<LinkDO> linkDOLambdaQueryWrapper = Wrappers.lambdaQuery(LinkDO.class)
                .eq(LinkDO::getGid, linkGotoDO.getGid())
                .eq(LinkDO::getFullShortUrl, fullShortUrl)
                .eq(LinkDO::getEnableStatus, 0)
                .eq(LinkDO::getDelFlag, 0);
        LinkDO linkDO = linkService.getOne(linkDOLambdaQueryWrapper);
        if (linkDO != null) {
            ((HttpServletResponse) response).sendRedirect(linkDO.getOriginUrl());
        }
    }

}
