package com.lazy.bike.controller;

import com.alibaba.fastjson.JSONObject;
import com.lazy.bike.pojo.Bike;
import com.lazy.bike.servce.BikeServce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author LaZY(李志一)
 * @create 2019-03-24 21:25
 */
@Controller
public class BikeController {
    @Autowired
    private BikeServce bikeService;

    @RequestMapping("/bike_list")
    public String list() {
        return "bike/list";
    }

    @RequestMapping("/bike_add")
    public String toAdd() {
        return "bike/add";
    }

    @RequestMapping("/bike_edit")
    public String toEdit() {
        return "bike/edit";
    }

    /***
     * 添加车辆
     * @param bike
     * @return
     */
    @PostMapping("/bike")
    @ResponseBody
    public String add(@RequestBody String bike) {
        Bike b = JSONObject.parseObject(bike, Bike.class);
        bikeService.save(b);
        return "success";
    }

    @PostMapping("/bike_edit")
    @ResponseBody
    public String edit(Bike bike) {
        bikeService.update(bike);
        return "success";
    }

    @DeleteMapping("/bike/{ids}")
    @ResponseBody
    public String deleteByIds(@PathVariable("ids") Long[] ids) {
        bikeService.deleteByIds(ids);
        return "success";
    }

    @GetMapping("/bike/{id}")
    @ResponseBody
    public Bike getById(@PathVariable("id") Long id) {
        return bikeService.getById(id);
    }


    /**
     * 查找当前坐标附近的单车
     * @param longitude
     * @param latitude
     * @return
     */
    @GetMapping("/bikes")
    @ResponseBody
    public GeoResults<Bike> findNear(double longitude, double latitude) {
        GeoResults<Bike> bikes = bikeService.findNear(longitude, latitude);
        return bikes;
    }

    @GetMapping("/allbikes")
    @ResponseBody
    public List<Bike> findAll() {
        return bikeService.findAll();
    }
////    @PostMapping("/bike")
////    @ResponseBody  //响应Ajax请求，会将响应的对象转成json
////    public String getById(@RequestBody String data) {
////        //(@RequestBody请求时结束json类型的数据
////        System.out.println(data);
////        return "succ";
////    }
//
//    @Autowired
//    private BikeServce bikeServce;
//
//
//    @GetMapping("/bike")
//    @ResponseBody  //响应Ajax请求，会将响应的对象转成json
//    public String getById(Bike bike) {
//        //调用Service保存map
//        bikeServce.save(bike);
//        return "success";
//    }
//
//    @PostMapping("/bike")
//    @ResponseBody
//    public String add(@RequestBody String bike) {
//        Bike b = JSONObject.parseObject(bike, Bike.class);
//        bikeServce.save(b);
//        return "success";
//    }
//
////    @GetMapping("/bikes")
////    @ResponseBody  //响应Ajax请求，会将响应的对象转成json
////    public List<Bike> findAll() {
////        List<Bike> bikes = bikeServce.findAll();
////        return bikes;
////    }
//    /**
//     * 查找当前坐标附近的单车
//     * @param longitude
//     * @param latitude
//     * @return
//     */
//    @GetMapping("/bikes")
//    @ResponseBody
//    public GeoResults<Bike> findNear(double longitude, double latitude) {
//        GeoResults<Bike> bikes = bikeServce.findNear(longitude, latitude);
//        return bikes;
//    }
//    //先跳转到视图页面
//    @GetMapping("/bike_list")
//    public String toList() {
//        return "bike/list";
//    }
}
