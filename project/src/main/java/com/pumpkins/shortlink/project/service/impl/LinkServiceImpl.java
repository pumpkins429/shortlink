package com.pumpkins.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.StrBuilder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pumpkins.shortlink.project.common.biz.user.UserContext;
import com.pumpkins.shortlink.project.common.convention.exception.ClientException;
import com.pumpkins.shortlink.project.common.convention.exception.ServiceException;
import com.pumpkins.shortlink.project.common.enums.LinkValidateTypeEnum;
import com.pumpkins.shortlink.project.dao.entity.LinkDO;
import com.pumpkins.shortlink.project.dao.mapper.LinkMapper;
import com.pumpkins.shortlink.project.dto.req.LinkCreateReqDTO;
import com.pumpkins.shortlink.project.dto.req.LinkPageReqDTO;
import com.pumpkins.shortlink.project.dto.req.LinkUpdateGroupReqDTO;
import com.pumpkins.shortlink.project.dto.req.LinkUpdateReqDTO;
import com.pumpkins.shortlink.project.dto.resp.*;
import com.pumpkins.shortlink.project.service.LinkService;
import com.pumpkins.shortlink.project.toolkit.HashUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    public LinkCreateRespDTO saveLink(LinkCreateReqDTO requestParam) {
        // TODO 校验网址格式等  域名应该由系统指定
        String fullShortLink = generateShortLink(requestParam);
        LinkDO linkDO = BeanUtil.toBean(requestParam, LinkDO.class);
        linkDO.setEnableStatus(0);
        linkDO.setUsername(UserContext.getUsername());
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

    /**
     * 分页查询短链
     * TODO 应该增加校验，验证用户身份，防止用户篡改请求参数中的gid直接查到其他用户的分组下的链接 link表应该也可以加上username字段，查的时候需要这两个条件就行。
     *
     * @param requestParam
     * @return
     */
    @Override
    public IPage<LinkPageRespDTO> queryLinkPage(LinkPageReqDTO requestParam) {
        LambdaQueryWrapper<LinkDO> wrapper = Wrappers.lambdaQuery(LinkDO.class)
                .eq(LinkDO::getGid, requestParam.getGid())
                // .eq(LinkDO::getEnableStatus, 1)
                .eq(LinkDO::getUsername, UserContext.getUsername())
                .orderByDesc(LinkDO::getCreateTime);
        IPage<LinkDO> resultPage = baseMapper.selectPage(requestParam, wrapper);
        return resultPage.convert(each -> BeanUtil.toBean(each, LinkPageRespDTO.class));
    }

    /**
     * 查询对应分组的短链数量
     *
     * @param gids
     * @return
     */
    @Override
    public List<LinkCountQueryRespDTO> queryLinkCount(List<String> gids) {
        // 使用Wrappers.query，基本类型的字段会自动加入where子句
        /* QueryWrapper<LinkDO> wrapper = Wrappers.query(new LinkDO())
                .select("gid as gid, count(*) as shortLinkCount")
                .in("gid", gids)
                .eq("enable_status", 0)
                .groupBy("gid"); */
        QueryWrapper<LinkDO> wrapper = new QueryWrapper<LinkDO>()
                .select("gid as gid, count(*) as shortLinkCount")
                .in("gid", gids)
                .eq("enable_status", 0)
                .groupBy("gid");
        List<Map<String, Object>> list = baseMapper.selectMaps(wrapper);
        return BeanUtil.copyToList(list, LinkCountQueryRespDTO.class);
    }

    /**
     * 更改短链信息
     * 不允许更改gid，更改分组需使用单独的接口
     *
     * @param requestParam
     * @return
     */
    @Override
    public LinkUpdateRespDTO updateLink(LinkUpdateReqDTO requestParam) {
        // TODO 存在穿透
        LambdaQueryWrapper<LinkDO> wrapper = Wrappers.lambdaQuery(LinkDO.class)
                .eq(LinkDO::getGid, requestParam.getGid())
                .eq(LinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq(LinkDO::getDelFlag, 0);
        LinkDO linkDO = baseMapper.selectOne(wrapper);
        if (linkDO == null) {
            throw new ClientException("短链不存在！");
        }
        LambdaUpdateWrapper<LinkDO> updateWrapper = Wrappers.lambdaUpdate(LinkDO.class)
                .eq(LinkDO::getGid, linkDO.getGid())
                .eq(LinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq(LinkDO::getDelFlag, 0)
                .set(LinkDO::getUpdateTime, new Date())
                // 如果有效期设为永久，则将有效期设置为空
                .set(Objects.equals(requestParam.getValidDateType(), LinkValidateTypeEnum.PERMANENT.getType()), LinkDO::getValidDate, null);
        // 暂时支持更改的信息为原始链接、有效时间、短链描述
        // TODO 这里的favicon，如果修改了原链接，也需要修改 后续描述也可以修改为自动爬取
        linkDO.setOriginUrl(requestParam.getOriginUrl())
                .setValidDateType(requestParam.getValidDateType())
                .setValidDate(Objects.equals(requestParam.getValidDateType(), LinkValidateTypeEnum.PERMANENT.getType()) ? null : requestParam.getValidDate())
                .setDescribe(requestParam.getDescribe());
        baseMapper.update(linkDO, updateWrapper);
        return BeanUtil.toBean(linkDO, LinkUpdateRespDTO.class);
    }

    /**
     * 更改短链分组
     *
     * @param requestParam
     * @return
     */
    @Transactional
    @Override
    public LinkUpdateGroupResqDTO updateLinkGroup(LinkUpdateGroupReqDTO requestParam) {
        LambdaQueryWrapper<LinkDO> wrapper = Wrappers.lambdaQuery(LinkDO.class)
                .eq(LinkDO::getGid, requestParam.getOriginGid())
                .eq(LinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq(LinkDO::getDelFlag, 0);
        LinkDO linkDO = baseMapper.selectOne(wrapper);
        if (linkDO == null) {
            throw new ClientException("短链不存在！");
        }
        if (requestParam.getOriginGid().equals(requestParam.getNewGid())) {
            return BeanUtil.toBean(linkDO, LinkUpdateGroupResqDTO.class);
        }

        // 删除旧的 移到新的
        LambdaUpdateWrapper<LinkDO> updateWrapper = Wrappers.lambdaUpdate(LinkDO.class)
                .eq(LinkDO::getGid, linkDO.getGid())
                .eq(LinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq(LinkDO::getDelFlag, 0);
        baseMapper.delete(updateWrapper);

        linkDO.setGid(requestParam.getNewGid());
        baseMapper.insert(linkDO);
        return BeanUtil.toBean(linkDO, LinkUpdateGroupResqDTO.class);
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
