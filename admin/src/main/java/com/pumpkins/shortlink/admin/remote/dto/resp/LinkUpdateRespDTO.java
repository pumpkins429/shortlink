package com.pumpkins.shortlink.admin.remote.dto.resp;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/*
 * @author      : pumpkins
 * @date        : 2024/7/21 16:41
 * @description : 更改短链返回参数
 * @Copyright   : ...
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LinkUpdateRespDTO {
    /**
     * 完整短链接
     */
    private String fullShortUrl;

    /**
     * 原始链接
     */
    private String originUrl;

    /**
     * 点击量
     */
    private Integer clickNum;

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 短链所属用户名
     */
    private String username;

    /**
     * 原始链接图标
     */
    private String favicon;

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
