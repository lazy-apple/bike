package com.lazy.bike.controller;

import com.lazy.bike.pojo.Bike;
import com.lazy.bike.servce.BikeServce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author LaZY(李志一)
 * @create 2019-03-24 21:25
 */
@Controller
public class BikeController {
    @PostMapping("/bike")
    @ResponseBody  //响应Ajax请求，会将响应的对象转成json
    public String getById(@RequestBody String data) {
        //(@RequestBody请求时结束json类型的数据
        System.out.println(data);
        return "succ";
    }
    @GetMapping("/test")
    public void test(){
        System.out.println(666);
    }

    @Autowired
    private BikeServce bikeServce;


//    @GetMapping("/bike")
//    @ResponseBody  //响应Ajax请求，会将响应的对象转成json
//    public String getById(Bike bike) {
//        //调用Service保存map
//        bikeServce.save(bike);
//        return "success";
//    }
}
