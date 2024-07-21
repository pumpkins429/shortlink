package com.pumpkins.shortlink.admin.remote.dto.req;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/*
 * @author      : pumpkins
 * @date        : 2024/7/21 16:42
 * @description : 更改短链请求参数
 * @Copyright   : ...
 */
@Data
public class LinkUpdateReqDTO {

    /**
     * 完整短链接 不允许修改
     */
    private String fullShortUrl;

    /**
     * 原始链接
     */
    private String originUrl;

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 有效期类型 0：永久有效 1：用户自定义
     */
    private int validDateType;

    /**
     * 有效期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validDate;

    /**
     * 描述
     */
    @TableField("`describe`")
    private String describe;

}
