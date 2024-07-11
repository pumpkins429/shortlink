package com.pumpkins.shortlink.admin.common.enums;

import com.pumpkins.shortlink.admin.common.convention.errorcode.IErrorCode;

/*
 * @author      : pumpkins
 * @date        : 2024/7/11 18:46
 * @description : ...
 * @Copyright   : ...
 */
public enum UserErrorCodeEnum implements IErrorCode {

    USER_NULL("B000200", "用户记录不存在"),
    USER_EXIST("B000201", "用户记录已存在");

    private final String code;

    private final String message;

    UserErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 错误码
     */
    @Override
    public String code() {
        return null;
    }

    /**
     * 错误信息
     */
    @Override
    public String message() {
        return null;
    }
}
