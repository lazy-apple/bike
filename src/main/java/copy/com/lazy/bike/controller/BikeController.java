package copy.com.lazy.bike.controller;

import com.alibaba.fastjson.JSONObject;
import copy.com.lazy.bike.pojo.Bike;
import copy.com.lazy.bike.servce.BikeServce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResults;
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
//    @PostMapping("/bike")
//    @ResponseBody  //响应Ajax请求，会将响应的对象转成json
//    public String getById(@RequestBody String data) {
//        //(@RequestBody请求时结束json类型的数据
//        System.out.println(data);
//        return "succ";
//    }

    @Autowired
    private BikeServce bikeServce;


    @GetMapping("/bike")
    @ResponseBody  //响应Ajax请求，会将响应的对象转成json
    public String getById(Bike bike) {
        //调用Service保存map
        bikeServce.save(bike);
        return "success";
    }

    @PostMapping("/bike")
    @ResponseBody
    public String add(@RequestBody String bike) {
        Bike b = JSONObject.parseObject(bike, Bike.class);
        bikeServce.save(b);
        return "success";
    }

//    @GetMapping("/bikes")
//    @ResponseBody  //响应Ajax请求，会将响应的对象转成json
//    public List<Bike> findAll() {
//        List<Bike> bikes = bikeServce.findAll();
//        return bikes;
//    }
    /**
     * 查找当前坐标附近的单车
     * @param longitude
     * @param latitude
     * @return
     */
    @GetMapping("/bikes")
    @ResponseBody
    public GeoResults<Bike> findNear(double longitude, double latitude) {
        GeoResults<Bike> bikes = bikeServce.findNear(longitude, latitude);
        return bikes;
    }
    //先跳转到视图页面
    @GetMapping("/bike_list")
    public String toList() {
        return "bike/list";
    }
}
