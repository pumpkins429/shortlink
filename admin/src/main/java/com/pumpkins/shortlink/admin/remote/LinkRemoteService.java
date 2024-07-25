package com.pumpkins.shortlink.admin.remote;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pumpkins.shortlink.admin.common.biz.user.UserContext;
import com.pumpkins.shortlink.admin.common.convention.exception.ClientException;
import com.pumpkins.shortlink.admin.common.convention.result.Result;
import com.pumpkins.shortlink.admin.common.convention.result.Results;
import com.pumpkins.shortlink.admin.remote.dto.req.*;
import com.pumpkins.shortlink.admin.remote.dto.resp.*;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.List;

/*
 * @author      : pumpkins
 * @date        : 2024/7/20 11:09
 * @description : 短链远程调用接口 TODO 后续用SpringCloud替换
 * @Copyright   : ...
 */
public interface LinkRemoteService {
    HttpClient CLIENT = HttpClient.newHttpClient();

    /**
     * 创建短链接
     *
     * @param requestParam
     * @return
     */
    default Result<LinkCreateRespDTO> saveLink(LinkCreateReqDTO requestParam) throws Exception {
        String url = "http://localhost:8001/api/short-link/v1/link";

        // 创建HttpRequest实例，设置请求方法、URL、头和请求体
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Content-Type", "application/json")
                .header("username", UserContext.getUsername())
                .header("token", UserContext.getToken())
                .method("POST", BodyPublishers.ofString(JSON.toJSONString(requestParam)))
                .build();

        // 发送请求并接收响应
        String resultJsonStr = CLIENT.send(request, BodyHandlers.ofString()).body();

        return JSON.parseObject(resultJsonStr, new TypeReference<>() {
        });
    }


    /**
     * 分页查询短链
     *
     * @param requestParam
     * @return
     */
    default Result<IPage<LinkPageRespDTO>> queryLinkPage(LinkPageReqDTO requestParam) throws Exception {
        String url = "http://localhost:8001/api/short-link/v1/link/page";

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("gid", requestParam.getGid());
        paramMap.put("current", requestParam.getCurrent());
        paramMap.put("size", requestParam.getSize());
        paramMap.put("username", requestParam.getUsername());

        // 创建HttpRequest实例，设置请求方法、URL、头和请求体
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Content-Type", "application/json")
                .header("username", UserContext.getUsername())
                .header("token", UserContext.getToken())
                .method("GET", BodyPublishers.ofString(JSON.toJSONString(paramMap)))
                .build();

        // 发送请求并接收响应
        String resultJsonStr = CLIENT.send(request, BodyHandlers.ofString()).body();
        return JSON.parseObject(resultJsonStr, new TypeReference<>() {
        });
    }

    /**
     * 查询对应分组的短链数量
     * @param gids gid 集合
     * @return
     */
    default Result<List<LinkCountQueryRespDTO>> queryLinkCount(List<String> gids){
        String url = "http://127.0.0.1:8001/api/short-link/v1/group-link-count";

        // 将 gids 转换为查询参数
        StringBuilder urlWithParams = new StringBuilder(url);
        urlWithParams.append("?");
        for (String gid : gids) {
            urlWithParams.append("gids=").append(gid).append("&");
        }
        // 去掉最后一个 '&'
        urlWithParams.deleteCharAt(urlWithParams.length() - 1);

        // 创建HttpRequest实例，设置请求方法、URL、头和请求体
        HttpRequest request;
        try{
             request = HttpRequest.newBuilder()
                    .uri(new URI(urlWithParams.toString()))
                    .header("Content-Type", "application/json")
                    .header("username", UserContext.getUsername())
                    .header("token", UserContext.getToken())
                    .GET()
                    .build();
        } catch (Exception e) {
            throw new ClientException("请求异常");
        }
        // 发送请求并接收响应
        String resultJsonStr;
        try {
            resultJsonStr = CLIENT.send(request, BodyHandlers.ofString()).body();
        } catch (Exception e) {
            throw new ClientException("请求异常");
        }
        return JSON.parseObject(resultJsonStr, new TypeReference<>() {
        });
    }

    /**
     * 修改短链接
     * @param requestParam
     * @return
     */
    default Result<LinkUpdateRespDTO> updateLink(LinkUpdateReqDTO requestParam) {
        String url = "http://localhost:8001/api/short-link/v1/link";

        // 创建HttpRequest实例，设置请求方法、URL、头和请求体
        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Content-Type", "application/json")
                    .header("username", UserContext.getUsername())
                    .header("token", UserContext.getToken())
                    .method("PUT", BodyPublishers.ofString(JSON.toJSONString(requestParam)))
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // 发送请求并接收响应
        String resultJsonStr = null;
        try {
            resultJsonStr = CLIENT.send(request, BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return JSON.parseObject(resultJsonStr, new TypeReference<>() {
        });
    }

    /**
     * 更新短链分组
     * @param requestParam
     * @return
     */
    default Result<LinkUpdateGroupResqDTO> updateLinkGroup(LinkUpdateGroupReqDTO requestParam) {
        String url = "http://localhost:8001/api/short-link/v1/link-group";

        // 创建HttpRequest实例，设置请求方法、URL、头和请求体
        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Content-Type", "application/json")
                    .header("username", UserContext.getUsername())
                    .header("token", UserContext.getToken())
                    .method("PUT", BodyPublishers.ofString(JSON.toJSONString(requestParam)))
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // 发送请求并接收响应
        String resultJsonStr = null;
        try {
            resultJsonStr = CLIENT.send(request, BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return JSON.parseObject(resultJsonStr, new TypeReference<>() {
        });
    }

    /**
     * 获取url标题
     * @param url
     * @return
     */
    @SneakyThrows
    default Result<String> getUrlTile(String url) {
        String requestUrl = "http://localhost:8001/api/short-link/v1/tile?url=" + url;

        // 创建HttpRequest实例，设置请求方法、URL、头和请求体
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(requestUrl))
                .header("Content-Type", "application/json")
                .header("username", UserContext.getUsername())
                .header("token", UserContext.getToken())
                .method("GET", BodyPublishers.noBody())
                .build();

        // 发送请求并接收响应
        String resultJsonStr = CLIENT.send(request, BodyHandlers.ofString()).body();
        return JSON.parseObject(resultJsonStr, new TypeReference<>() {
        });
    }

    /**
     * TODO 短链移至回收站
     * @param requestParam
     * @return
     */
    default Result<Void> moveToRecycleBin(LinkMoveToRecycleBInReqDTO requestParam) {
        return Results.success();
    }

}
