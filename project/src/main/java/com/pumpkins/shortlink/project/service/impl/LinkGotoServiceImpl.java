package com.pumpkins.shortlink.project.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pumpkins.shortlink.project.common.constant.LinkConstants;
import com.pumpkins.shortlink.project.common.constant.RedisCacheConstants;
import com.pumpkins.shortlink.project.common.convention.exception.ClientException;
import com.pumpkins.shortlink.project.dao.entity.LinkAccessStatsDO;
import com.pumpkins.shortlink.project.dao.entity.LinkDO;
import com.pumpkins.shortlink.project.dao.entity.LinkGotoDO;
import com.pumpkins.shortlink.project.dao.entity.LinkLocaleStatsDO;
import com.pumpkins.shortlink.project.dao.mapper.LinkGotoMapper;
import com.pumpkins.shortlink.project.service.LinkAccessStatsService;
import com.pumpkins.shortlink.project.service.LinkGotoService;
import com.pumpkins.shortlink.project.service.LinkLocaleStatsService;
import com.pumpkins.shortlink.project.service.LinkService;
import com.pumpkins.shortlink.project.toolkit.LinkUtil;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private final LinkAccessStatsService linkAccessStatsService;
    private final LinkLocaleStatsService linkLocaleStatsService;

    // 高德key
    @Value("${short-link.stats.amap-key}")
    private String amap_key;

    // 域名
    @Value("${short-link.domain}")
    String domain;

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
            shortLinkAccessStats(shortUri, request, response);
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
                shortLinkAccessStats(shortUri, request, response);
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
                    .eq(LinkDO::getEnableStatus, 1)
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
                shortLinkAccessStats(shortUri, request, response);
                ((HttpServletResponse) response).sendRedirect(linkDO.getOriginUrl());
            } else {
                stringRedisTemplate.opsForValue()
                        .set(RedisCacheConstants.SHORT_LINK_GOTO_ISNULL_KEY + fullShortUrl, RedisCacheConstants.SHORT_LINK_GOTO_NULL_VALUE, 30, TimeUnit.SECONDS);
                ((HttpServletResponse) response).sendRedirect(LinkConstants.SHORT_LINK_NOT_FOUND_PAGE);
            }
        } finally {
            linkLock.unlock();
        }

    }

    /**
     * 短链跳转监控统计
     * 通过cookie判断uv
     */
    private void shortLinkAccessStats(String shortUri, ServletRequest request, ServletResponse response) {
        AtomicBoolean uvFirstFlag = new AtomicBoolean();
        Cookie[] cookies = ((HttpServletRequest) request).getCookies();
        // 第一次访问时返回带uv标识的cookie
        Runnable addResponseCookieTask = () -> {
            String uv = UUID.fastUUID().toString();
            Cookie uvCookie = new Cookie("uv", uv);
            uvCookie.setMaxAge(60 * 60 * 24 * 31);
            uvCookie.setPath(StrUtil.sub(shortUri, shortUri.lastIndexOf("/"), shortUri.length()));
            ((HttpServletResponse) response).addCookie(uvCookie);
            uvFirstFlag.set(Boolean.TRUE);
            stringRedisTemplate.opsForSet().add(RedisCacheConstants.SHORT_LINK_STATS_UV + shortUri, uv);
        };

        // 判断用户是否第一次访问
        if (ArrayUtil.isNotEmpty(cookies)) {
            Arrays.stream(cookies)
                    .filter(each -> Objects.equals(each.getName(), "uv"))
                    .findFirst()
                    .map(Cookie::getValue)
                    .ifPresentOrElse(each -> {
                                Long uvAdded = stringRedisTemplate.opsForSet().add(RedisCacheConstants.SHORT_LINK_STATS_UV + shortUri, each);
                                uvFirstFlag.set(uvAdded != null && uvAdded > 0L);
                            }, addResponseCookieTask
                    );
        } else {
            addResponseCookieTask.run();
        }
        // 判断ip
        String ip = LinkUtil.getIp((HttpServletRequest) request);
        Long uipAdded = stringRedisTemplate.opsForSet().add(RedisCacheConstants.SHORT_LINK_STATS_UIP + shortUri, ip);

        Date date = new Date();
        LinkAccessStatsDO linkAccessStatsDO = LinkAccessStatsDO.builder()
                .fullShortUrl(shortUri)
                .date(date)
                .pv(1)
                .uv(uvFirstFlag.get() ? 1 : 0)
                .uip(uipAdded != null && uipAdded > 0 ? 1 : 0)
                .hour(DateUtil.hour(date, true))
                .weekday(DateUtil.dayOfWeekEnum(date).getIso8601Value())
                .build();
        try {
            linkAccessStatsService.recordAccessStats(shortUri, request, linkAccessStatsDO);
        } catch (Throwable e) {
            log.error("短链接访问量统计异常", e);
        }

        // 地区监控 TODO 待重构优化 比如这里请求高德接口，不应该每次请求都创建新对象；以及这里可以使用异步更新数据库
        HashMap<String, Object> param = new HashMap<>();
        param.put("key", amap_key);
        param.put("ip", ip);
        String url = LinkConstants.SHORT_LINK_AMAP_URL;
        String resultJson = HttpUtil.get(url, param);
        JSONObject jsonObject = JSON.parseObject(resultJson);

        String infocode = jsonObject.getString("infocode");
        if (StrUtil.isNotBlank(infocode) && StrUtil.equals(infocode, "10000")) {
            String province = jsonObject.getString("province");// 可能为[]
            String city = jsonObject.getString("city");
            String adcode = jsonObject.getString("adcode");

            // 查询分组 TODO 待优化，应该传参得到gid
            String gid = baseMapper.selectOne(Wrappers.lambdaQuery(LinkGotoDO.class).eq(LinkGotoDO::getFullShortUrl, domain + "/" + shortUri)).getGid();

            // TODO 暂时不设置gid；这里国家设为CN，因为高德api只支持中国，可以后续替换其他接口
            boolean emptyFlag = false;
            if ("[]".equals(adcode)) {
                emptyFlag = true;
            }
            LinkLocaleStatsDO linkLocaleStatsDO = LinkLocaleStatsDO.builder()
                    .fullShortUrl(shortUri)
                    .gid(gid)
                    .date(new Date())
                    .cnt(1)
                    .province(emptyFlag ? "" : province)
                    .city(emptyFlag ? "" : city)
                    .adcode(emptyFlag ? "" : adcode)
                    .country("CN")
                    .build();

            try {
                linkLocaleStatsService.recordLocaleStats(linkLocaleStatsDO);
            } catch (Throwable e) {
                log.error("短链访问地区统计数据库保存异常", e);
            }

        } else {
            throw new ClientException("短链访问地区统计请求API异常");
        }
    }


}
