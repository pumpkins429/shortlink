package com.pumpkins.shortlink.project.common.convention.errorcode;/*
 * @author      : pumpkins
 * @date        : 2024/7/11 18:38
 * @description : 平台错误码
 * @Copyright   : ...
 */

public interface IErrorCode {
    /**
     * 错误码
     */
    String code();

    /**
     * 错误信息
     */
    String message();
}
