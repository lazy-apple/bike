package com.lazy.bike.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {


    //请求映射，跟浏览器中的URL请求资源对应就会调用对应的发放
    @RequestMapping("/")
    public String index() {
        //前缀 + 视图名 + 后缀
        //pages/index.html
        return "index";
    }
}
