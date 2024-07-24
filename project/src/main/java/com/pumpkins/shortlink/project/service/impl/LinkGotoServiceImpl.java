package com.pumpkins.shortlink.project.service.impl;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pumpkins.shortlink.project.common.constant.LinkConstants;
import com.pumpkins.shortlink.project.common.constant.RedisCacheConstants;
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
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

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

    private final RBloomFilter<String> shortlinkCachePenetrationBloomFilter;
    private final LinkService linkService;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedissonClient redissonClient;

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

        String originUrl = stringRedisTemplate.opsForValue().get(RedisCacheConstants.SHORT_LINK_GOTO_KEY + fullShortUrl);
        if (StrUtil.isNotBlank(originUrl)) {
            ((HttpServletResponse) response).sendRedirect(originUrl);
            return;
        }

        // 查看布隆过滤器
        if (!shortlinkCachePenetrationBloomFilter.contains(fullShortUrl)) {
            // 布隆过滤器中不存在说明数据库中肯定不存在
            ((HttpServletResponse) response).sendRedirect(LinkConstants.SHORT_LINK_NOT_FOUND_PAGE);
            return;
        }

        // 防止布隆过滤器误判
        String shortLinkGotoIsNull = stringRedisTemplate.opsForValue().get(RedisCacheConstants.SHORT_LINK_GOTO_ISNULL_KEY + fullShortUrl);
        if (RedisCacheConstants.SHORT_LINK_GOTO_NULL_VALUE.equals(shortLinkGotoIsNull)) {
            ((HttpServletResponse) response).sendRedirect(LinkConstants.SHORT_LINK_NOT_FOUND_PAGE);
            return;
        }

        // 获取分布式锁
        RLock linkLock = redissonClient.getLock(RedisCacheConstants.LOCK_SHORT_LINK_GOTO_KEY + fullShortUrl);
        linkLock.lock();
        try {
            // 双重检查锁
            originUrl = stringRedisTemplate.opsForValue().get(RedisCacheConstants.SHORT_LINK_GOTO_KEY + fullShortUrl);
            if (StrUtil.isNotBlank(originUrl)) {
                ((HttpServletResponse) response).sendRedirect(originUrl);
                return;
            }

            // 查数据库
            // 查询路由表
            LambdaQueryWrapper<LinkGotoDO> linkGotoDOLambdaQueryWrapper = Wrappers.lambdaQuery(LinkGotoDO.class)
                    .eq(LinkGotoDO::getFullShortUrl, fullShortUrl);
            LinkGotoDO linkGotoDO = baseMapper.selectOne(linkGotoDOLambdaQueryWrapper);
            if (linkGotoDO == null) {
                // TODO 此处应该进行风控
                stringRedisTemplate.opsForValue()
                        .set(RedisCacheConstants.SHORT_LINK_GOTO_ISNULL_KEY + fullShortUrl, RedisCacheConstants.SHORT_LINK_GOTO_NULL_VALUE, 30, TimeUnit.SECONDS);
                ((HttpServletResponse) response).sendRedirect(LinkConstants.SHORT_LINK_NOT_FOUND_PAGE);
                return;
            }
            LambdaQueryWrapper<LinkDO> linkDOLambdaQueryWrapper = Wrappers.lambdaQuery(LinkDO.class)
                    .eq(LinkDO::getGid, linkGotoDO.getGid())
                    .eq(LinkDO::getFullShortUrl, fullShortUrl)
                    .eq(LinkDO::getEnableStatus, 0)
                    .eq(LinkDO::getDelFlag, 0);
            LinkDO linkDO = linkService.getOne(linkDOLambdaQueryWrapper);
            if (linkDO != null) {
                // 判断是否过期 过期就设置空值
                if (linkDO.getValidDate() != null && linkDO.getValidDate().before(new Date())) {
                    stringRedisTemplate.opsForValue().set(RedisCacheConstants.SHORT_LINK_GOTO_ISNULL_KEY + fullShortUrl, RedisCacheConstants.SHORT_LINK_GOTO_NULL_VALUE, 30, TimeUnit.MINUTES);
                    ((HttpServletResponse) response).sendRedirect(LinkConstants.SHORT_LINK_NOT_FOUND_PAGE);
                    return;
                }
                // 保存到缓存中
                stringRedisTemplate.opsForValue().set(RedisCacheConstants.SHORT_LINK_GOTO_KEY + fullShortUrl, linkDO.getOriginUrl(), 30, TimeUnit.MINUTES);
                ((HttpServletResponse) response).sendRedirect(linkDO.getOriginUrl());
            }
        } finally {
            linkLock.unlock();
        }

    }

}
