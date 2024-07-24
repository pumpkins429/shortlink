package com.pumpkins.shortlink.project.service.impl;

import com.pumpkins.shortlink.project.service.UrlTitleService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;

/*
 * @author      : pumpkins
 * @date        : 2024/7/24 14:02
 * @description : 短链标题接口实现层
 * @Copyright   : ...
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UrlTitleServiceImpl implements UrlTitleService {
    /**
     * 获取url标题
     *
     * @param url
     * @return
     */
    @SneakyThrows
    @Override
    public String getUrlTile(String url) {
        URL tagetUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) tagetUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            Document document = Jsoup.connect(url).get();
            return document.title();
        }

        return "Error while fetching title";
    }
}
