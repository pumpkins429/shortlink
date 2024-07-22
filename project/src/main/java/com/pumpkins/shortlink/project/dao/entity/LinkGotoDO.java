package com.pumpkins.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/*
 * @author      : pumpkins
 * @date        : 2024/7/22 14:24
 * @description : 短链路由实体类
 * @Copyright   : ...
 */
@Data
@TableName("t_link_goto")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LinkGotoDO {
    /**
     * id
     */
    private Long id;

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 完整短链接
     */
    private String fullShortUrl;
}
