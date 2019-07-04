package copy.com.lazy.bike.controller;

import copy.com.lazy.bike.pojo.User;
import copy.com.lazy.bike.servce.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    //请求映射，跟浏览器中的URL请求资源对应就会调用对应的发放
    @RequestMapping("/")
    public String index() {
        //前缀 + 视图名 + 后缀
        //pages/index.html
        return "index";
    }
    //请求映射，跟浏览器中的URL请求资源对应就会调用对应的发放
    @RequestMapping("/host")
    @ResponseBody
    public String host() {
        String host = null;
        try {
            host = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return host;
    }

    /***
     * 验证手机号验证码
     * @param user
     * @return
     */
    @PostMapping("/verify")
    @ResponseBody
    public boolean verify(User user) {
        boolean flag = userService.verify(user);
        return flag;
    }

    /***
     * 通过手机号获取验证码
     * @param nationCode
     * @param phoneNum
     * @return
     */
    @PostMapping("/genCode")
    @ResponseBody
    public String genCode(String nationCode, String phoneNum) {
        String msg = "true";
        try {
            //生成4位随机数 -> 调用短信接口发送验证码 -> 将手机号对应的验证码保存到redis中，并且设置这个key的有效时长
            userService.genVerifyCode(nationCode, phoneNum);
        } catch (Exception e) {
            e.printStackTrace();
            msg = "false";
        }
        return msg;
    }
    @GetMapping("/phoneNum/{openid}")
    @ResponseBody
    public User getPhoneNum(@PathVariable("openid") String openid) {
        User user = userService.getUserByOpenid(openid);
        return user;
    }
    @PostMapping("/recharge")
    @ResponseBody
    public boolean recharge(@RequestBody String params) {
        boolean flag = true;
        //System.out.println(params);
        userService.recharge(params);
        return flag;
    }
    @PostMapping("/deposit")
    @ResponseBody
    public String deposit(User user) {
        userService.deposit(user);
        return "success";
    }
    @PostMapping("/identify")
    @ResponseBody
    public String identify(User user) {
        userService.identify(user);
        return "success";
    }
}
