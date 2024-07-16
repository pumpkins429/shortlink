package com.pumpkins.shortlink.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pumpkins.shortlink.project.dao.entity.LinkDO;
import com.pumpkins.shortlink.project.dao.mapper.LinkMapper;
import com.pumpkins.shortlink.project.service.LinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}
