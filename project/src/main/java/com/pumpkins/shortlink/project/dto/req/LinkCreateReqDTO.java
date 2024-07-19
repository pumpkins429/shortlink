package com.pumpkins.shortlink.project.dto.req;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/*
 * @author      : pumpkins
 * @date        : 2024/7/16 14:56
 * @description : 短链创建请求实体
 * @Copyright   : ...
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkCreateReqDTO {

    /**
     * 域名
     */
    private String domain;


    /**
     * 原始链接
     */
    private String originUrl;


    /**
     * 分组标识
     */
    private String gid;

    /**
     * 原始链接图标
     */
    private String favicon;


    /**
     * 创建类型 0：控制台 1：接口
     */
    private int createdType;

    /**
     * 有效期类型 0：永久有效 1：用户自定义
     */
    private int validDateType;

    /**
     * 有效期
     */
    private Date validDate;

    /**
     * 描述
     */
    @TableField("`describe`")
    private String describe;

}
