package com.pumpkins.shortlink.admin.remote.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.pumpkins.shortlink.admin.common.biz.user.UserContext;
import com.pumpkins.shortlink.admin.common.convention.result.Result;
import lombok.SneakyThrows;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/*
 * @author      : pumpkins
 * @date        : 2024/7/25 23:47
 * @description : ...
 * @Copyright   : ...
 */
public class LinkHttpUtil {

    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    /**
     * 发送请求
     * 请求头携带username和token
     *
     * @param requestType
     * @param url
     * @param requestParam
     * @param typeReference
     * @param <T>
     * @return
     */
    @SneakyThrows
    public static <T> Result<T> request(String requestType, String url, Object requestParam, TypeReference<Result<T>> typeReference) {
        // String url = "http://localhost:8001/api/...";

        // 创建HttpRequest实例，设置请求方法、URL、头和请求体
        HttpRequest request = null;
        request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Content-Type", "application/json")
                .header("username", UserContext.getUsername())
                .header("token", UserContext.getToken())
                .method(requestType, HttpRequest.BodyPublishers.ofString(JSON.toJSONString(requestParam)))
                .build();

        // 发送请求并接收响应
        String resultJsonStr = null;
        resultJsonStr = CLIENT.send(request, HttpResponse.BodyHandlers.ofString()).body();

        return JSON.parseObject(resultJsonStr, typeReference);
    }

    @SneakyThrows
    public static <T> Result<T> GetWithUrlParams(String url, String requestParamsName, List<String> requestParams, TypeReference<Result<T>> typeReference) {
        // String url = "http://127.0.0.1:8001/api/...";

        // 将 requestParams 转换为查询参数
        StringBuilder urlWithParams = new StringBuilder(url);
        urlWithParams.append("?");
        for (String param : requestParams) {
            urlWithParams.append(requestParamsName + "=").append(param).append("&");
        }
        // 去掉最后一个 '&'
        urlWithParams.deleteCharAt(urlWithParams.length() - 1);

        // 创建HttpRequest实例，设置请求方法、URL、头和请求体
        HttpRequest request;
        request = HttpRequest.newBuilder()
                .uri(new URI(urlWithParams.toString()))
                .header("Content-Type", "application/json")
                .header("username", UserContext.getUsername())
                .header("token", UserContext.getToken())
                .GET()
                .build();

        // 发送请求并接收响应
        String resultJsonStr;
        resultJsonStr = CLIENT.send(request, HttpResponse.BodyHandlers.ofString()).body();

        return JSON.parseObject(resultJsonStr, typeReference);
    }
}
