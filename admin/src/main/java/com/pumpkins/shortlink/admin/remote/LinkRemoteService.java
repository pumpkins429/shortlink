package com.pumpkins.shortlink.admin.remote;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pumpkins.shortlink.admin.common.convention.result.Result;
import com.pumpkins.shortlink.admin.remote.dto.req.*;
import com.pumpkins.shortlink.admin.remote.dto.resp.*;
import com.pumpkins.shortlink.admin.remote.util.LinkHttpUtil;
import lombok.SneakyThrows;

import java.net.http.HttpClient;
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
        return LinkHttpUtil.request(
                "POST",
                "http://localhost:8001/api/short-link/v1/link",
                requestParam,
                new TypeReference<>() {
                }
        );
    }


    /**
     * 分页查询短链
     *
     * @param requestParam
     * @return
     */
    default Result<IPage<LinkPageRespDTO>> queryLinkPage(LinkPageReqDTO requestParam) throws Exception {
        return LinkHttpUtil.request(
                "GET",
                "http://localhost:8001/api/short-link/v1/link/page",
                requestParam,
                new TypeReference<>() {
                }
        );
    }

    /**
     * 查询对应分组的短链数量
     *
     * @param gids gid 集合
     * @return
     */
    default Result<List<LinkCountQueryRespDTO>> queryLinkCount(List<String> gids) {
        return LinkHttpUtil.GetWithUrlParams(
                "http://127.0.0.1:8001/api/short-link/v1/group-link-count",
                "gids",
                gids,
                new TypeReference<>() {
                }
        );
    }

    /**
     * 修改短链接
     *
     * @param requestParam
     * @return
     */
    default Result<LinkUpdateRespDTO> updateLink(LinkUpdateReqDTO requestParam) {
        return LinkHttpUtil.request(
                "PUT",
                "http://localhost:8001/api/short-link/v1/link",
                requestParam,
                new TypeReference<>() {
                }
        );
    }

    /**
     * 更新短链分组
     *
     * @param requestParam
     * @return
     */
    default Result<LinkUpdateGroupResqDTO> updateLinkGroup(LinkUpdateGroupReqDTO requestParam) {
        return LinkHttpUtil.request(
                "PUT",
                "http://localhost:8001/api/short-link/v1/link-group",
                requestParam,
                new TypeReference<>() {
                }
        );
    }

    /**
     * 获取url标题
     *
     * @param url
     * @return
     */
    @SneakyThrows
    default Result<String> getUrlTile(String url) {
        return LinkHttpUtil.GetWithUrlParams(
                "http://localhost:8001/api/short-link/v1/tile",
                "url",
                ListUtil.toList(url),
                new TypeReference<>() {
                }
        );
    }

    /**
     * 短链移至回收站
     *
     * @param requestParam
     * @return
     */
    default Result<Void> moveToRecycleBin(LinkMoveToRecycleBinReqDTO requestParam) {
        return LinkHttpUtil.request(
                "POST",
                "http://localhost:8001/api/short-link/v1/recycleBin/recycle",
                requestParam,
                new TypeReference<>() {
                }
        );
    }

    /**
     * 查询分组下的移入回收站中的短链
     *
     * @param requestParam
     * @return
     */
    default Result<IPage<LinkRecycleBinPageRespDTO>> queryRecycleBinPage(LinkRecycleBinPageReqDTO requestParam) {
         return LinkHttpUtil.request(
                "GET",
                "http://localhost:8001/api/short-link/v1/recycleBin/page",
                requestParam,
                new TypeReference<>() {
                }
        );
    }



}
