package com.pumpkins.shortlink.project.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @author      : pumpkins
 * @date        : 2024/7/16 14:57
 * @description : 短链创建返回对象
 * @Copyright   : ...
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkCreateRespDTO {

    /**
     * 分组标识
     */
    private String gid;



    /**
     * 完整短链接
     */
    private String fullShortUrl;

    /**
     * 原始链接
     */
    private String originUrl;




}
