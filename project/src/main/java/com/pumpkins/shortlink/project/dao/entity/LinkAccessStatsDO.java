package com.pumpkins.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pumpkins.shortlink.project.common.database.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/*
 * @author      : pumpkins
 * @date        : 2024/7/29 16:11
 * @description : 短链访问监控实体类
 * @Copyright   : ...
 */
@Data
@TableName("t_link_access_stats")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkAccessStatsDO extends BaseDO {

    /**
     * id
     */
    private Long id;

    /**
     * 完整短链接
     */
    private String fullShortUrl;

    /**
     * 日期
     */
    private Date date;

    /**
     * 访问量
     */
    private Integer pv;

    /**
     * 独立访问数
     */
    private Integer uv;

    /**
     * 独立ip数
     */
    private Integer uip;

    /**
     * 小时
     */
    private Integer hour;

    /**
     * 星期
     */
    private Integer weekday;

}