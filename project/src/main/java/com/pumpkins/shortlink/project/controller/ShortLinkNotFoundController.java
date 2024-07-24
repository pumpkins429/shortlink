package com.pumpkins.shortlink.project.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/*
 * @author      : pumpkins
 * @date        : 2024/7/24 10:54
 * @description : 短链接不存在跳转控制器
 * @Copyright   : ...
 */

@Controller
public class ShortLinkNotFoundController {

    /**
     * 短链接不存在跳转页面
     */
    @RequestMapping("/page/notfound")
    public String notfound() {
        return "notfound";
    }
}