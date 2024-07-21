package com.pumpkins.shortlink.project.common.enums;
/*
 * @author      : pumpkins
 * @date        : 2024/7/21 17:21
 * @description : 短链有效期类型枚举类
 * @Copyright   : ...
 */

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter
public enum LinkValidateTypeEnum {

    /**
     * 永久有效
     */
    PERMANENT(0),
    /**
     * 自定义
     */
    CUSTOM(1);

    private final int type;
}
