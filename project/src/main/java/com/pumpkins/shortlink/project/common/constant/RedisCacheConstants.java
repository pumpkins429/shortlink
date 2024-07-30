package com.pumpkins.shortlink.project.common.constant;

/*
 * @author      : pumpkins
 * @date        : 2024/7/12 17:30
 * @description : 后管系统redis常用变量
 * @Copyright   : ...
 */
public class RedisCacheConstants {

    public static final String LOCK_USER_REGISTER_KEY = "short-link:lock_user-register:";
    public static final String USER_LOGIN_TOKEN = "short-link:user_login_token:";

    /**
     * 短链跳转前缀
     */
    public static final String LOCK_SHORT_LINK_GOTO_KEY = "short-link:lock_short_link_goto:";
    /**
     * 短链跳转锁前缀
     * key为短链
     * value为对应原始链接
     */
    public static final String SHORT_LINK_GOTO_KEY = "short-link:short_link_goto:";

    /**
     * 短链空值前缀
     */
    public static final String SHORT_LINK_GOTO_ISNULL_KEY = "short-link:short_link_goto_is_null:";
    /**
     * 代表短链不存在的空值
     */
    public static final String SHORT_LINK_GOTO_NULL_VALUE = "-";

    /**
     * 永久有效的短链的缓存默认有效时间，单位为毫秒
     */
    public static final Long SHORT_LINK_DEFAULT_VALIDATE_TIME = 2629800000L;


    /**
     * UV
     */
    public static final String SHORT_LINK_STATS_UV = "short-link:stats:uv:";
    /**
     * UIP
     */
    public static final String SHORT_LINK_STATS_UIP = "short-link:stats:uip:";

}
