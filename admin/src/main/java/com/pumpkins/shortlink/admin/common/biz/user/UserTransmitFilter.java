package com.pumpkins.shortlink.admin.common.biz.user;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Lists;
import com.pumpkins.shortlink.admin.common.constant.RedisCacheConstants;
import com.pumpkins.shortlink.admin.common.constant.UserConstant;
import com.pumpkins.shortlink.admin.common.convention.exception.ClientException;
import com.pumpkins.shortlink.admin.common.convention.result.Results;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 用户信息传输过滤器
 **/
@Slf4j
@RequiredArgsConstructor
public class UserTransmitFilter implements Filter {

    private final StringRedisTemplate stringRedisTemplate;

    private static final List<String> IGNORE_URI = Lists.newArrayList(
            "/api/short-link/admin/v1/user/login",
            "/api/short-link/admin/v1/has-username");

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String method = httpServletRequest.getMethod();
        String requestURI = httpServletRequest.getRequestURI();
        if (!IGNORE_URI.contains(requestURI)) {
            if (!("/api/short-link/admin/v1/user".equals(requestURI) && "POST".equalsIgnoreCase(method))) {
                String userName = httpServletRequest.getHeader(UserConstant.USER_NAME_KEY);
                if (StringUtils.hasText(userName)) {
                    userName = URLDecoder.decode(userName, UTF_8);
                }
                String token = httpServletRequest.getHeader(UserConstant.USER_TOKEN_KEY);
                if (!StrUtil.isAllNotBlank(userName, token)) {
                    returnJson(servletResponse, JSON.toJSONString(Results.failure(new ClientException("请求用户名或token错误"))));
                    return;
                }
                String userInfoJsonStr = (String) stringRedisTemplate.opsForHash().get(RedisCacheConstants.USER_LOGIN_TOKEN + userName, token);
                if (null != userInfoJsonStr) {
                    UserInfoDTO userInfoDTO = JSON.parseObject(userInfoJsonStr, UserInfoDTO.class);
                    userInfoDTO.setToken(token);
                    UserContext.setUser(userInfoDTO);
                } else {
                    returnJson(servletResponse, JSON.toJSONString(Results.failure(new ClientException("请求用户名或token错误"))));
                    return;
                }
            }
        }
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            // 释放，防止内存泄漏
            UserContext.removeUser();
        }
    }

    /**
     * 全局异常处理器无法拦截过滤器抛出的异常，这里手动在response写入异常信息
     * @param response
     * @param json
     */
    private void returnJson(ServletResponse response, String json) {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(json);

        } catch (IOException e) {
            log.error("response error", e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

}