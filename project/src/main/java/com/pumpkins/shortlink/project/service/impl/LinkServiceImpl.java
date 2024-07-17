package com.pumpkins.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.StrBuilder;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pumpkins.shortlink.project.common.convention.exception.ServiceException;
import com.pumpkins.shortlink.project.dao.entity.LinkDO;
import com.pumpkins.shortlink.project.dao.mapper.LinkMapper;
import com.pumpkins.shortlink.project.dto.req.LinkCreateReqDTO;
import com.pumpkins.shortlink.project.dto.resp.LinkCreateRespDTO;
import com.pumpkins.shortlink.project.service.LinkService;
import com.pumpkins.shortlink.project.toolkit.HashUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

/*
 * @author      : pumpkins
 * @date        : 2024/7/16 14:23
 * @description : 短链接口实现类
 * @Copyright   : ...
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LinkServiceImpl extends ServiceImpl<LinkMapper, LinkDO> implements LinkService {

    private final RBloomFilter<String> bloomFilter;

    /**
     * 创建短链接
     *
     * @param requestParam
     * @return
     */
    @Override
    public LinkCreateRespDTO createLink(LinkCreateReqDTO requestParam) {
        // TODO 校验网址格式等
        String fullShortLink = generateShortLink(requestParam);
        LinkDO linkDO = BeanUtil.toBean(requestParam, LinkDO.class);
        linkDO.setEnableStatus(0);
        linkDO.setShortUri(fullShortLink.substring(fullShortLink.lastIndexOf("/") + 1));
        linkDO.setFullShortUrl(fullShortLink);
        try {
            baseMapper.insert(linkDO);
        } catch (DuplicateKeyException e) {
            bloomFilter.add(fullShortLink); // 防止布隆过滤器误判，防刷
            throw new ServiceException("数据库插入记录失败");
        }

        bloomFilter.add(fullShortLink);
        return LinkCreateRespDTO.builder()
                .gid(linkDO.getGid())
                .fullShortUrl(linkDO.getFullShortUrl())
                .originUrl(requestParam.getOriginUrl())
                .build();
    }

    private String generateShortLink(LinkCreateReqDTO requestParam) {
        String fullShortLink;
        int count = 0;
        String salt = ""; // 加盐
        do {
            if (count > 10) {
                throw new ServiceException("创建短链频繁，请稍后重试");
            }
            // fullShortLink = requestParam.getDomain() + "/" + HashUtil.hashToBase62(requestParam.getOriginUrl() + salt);
            fullShortLink = StrBuilder.create()
                    .append(requestParam.getDomain())
                    .append("/")
                    .append(HashUtil.hashToBase62(requestParam.getOriginUrl() + salt))
                    .toString();
            if (!bloomFilter.contains(fullShortLink)) {
                break;
            }
            salt = String.valueOf(System.currentTimeMillis()); // 加盐
            count++;
        } while (true);
        return fullShortLink;
    }
}
