package com.pumpkins.shortlink.project.toolkit;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.pumpkins.shortlink.project.common.constant.RedisCacheConstants;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.Optional;

/*
 * @author      : pumpkins
 * @date        : 2024/7/23 21:35
 * @description : 短链工具类
 * @Copyright   : ...
 */
public class LinkUtil {
    /**
     * 生成短链缓存过期时间
     * 如果validate为null说明为永久有效的短链，设置缓存过期时间为半个月
     * @param validDate
     * @return
     */
    public static long getShortLinkCacheExpireTime(Date validDate) {
        // TODO 如果有效时间过期的处理逻辑
        return Optional.ofNullable(validDate)
                .map(each -> DateUtil.between(new Date(), validDate, DateUnit.MS))
                .orElse(RedisCacheConstants.SHORT_LINK_DEFAULT_VALIDATE_TIME);
    }

    /**
     * 获取请求的真实 IP 地址
     * @param request
     * @return
     */
    public static String getIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");

        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }

        return ipAddress;
    }
}
