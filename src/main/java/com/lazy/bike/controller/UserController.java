package com.lazy.bike.controller;

import com.lazy.bike.pojo.User;
import com.lazy.bike.servce.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    //请求映射，跟浏览器中的URL请求资源对应就会调用对应的发放
    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/reg")
    @ResponseBody
    public String register(@RequestBody String params) {
        System.out.println(params);
        //userService.register(params);
        return "success";
    }

    /***
     * 接收验证码
     * @param user
     * @return
     */
    @PostMapping("/verify")
    @ResponseBody
    public boolean verify(User user) {
        boolean flag = userService.verify(user);
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

    @GetMapping("/phoneNum/{openid}")
    @ResponseBody
    public User getPhoneNum(@PathVariable("openid") String openid) {
        User user = userService.getUserByOpenid(openid);
        return user;
    }

    /***
     * 发送验证码
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



    @PostMapping("/recharge")
    @ResponseBody
    public boolean recharge(@RequestBody String params) {
        boolean flag = true;
        //System.out.println(params);
        userService.recharge(params);
        return flag;
    }


    @RequestMapping("/welcome")
    public String welcome() {
        return "user/welcome";
    }

    //方法上没有加ResponseBoy，那么转发到指定名字的视图上
    @RequestMapping("/user_list")
    public String list() {
        return "user/list";
    }

    @RequestMapping("/user_add")
    public String toAdd() {
        return "user/add";
    }

    @RequestMapping("/user_edit")
    public String toEdit() {
        return "user/edit";
    }

    @PostMapping("/user")
    //通过Ajax方式进行请求，页面不跳转
    @ResponseBody
    public String add(User user) {
        userService.save(user);
        return "success";
    }

    @PostMapping("/user_edit")
    @ResponseBody
    public String edit(User user) {
        userService.update(user);
        return "success";
    }

    @DeleteMapping("/user/{ids}")
    @ResponseBody
    public String deleteByIds(@PathVariable("ids") Long[] ids) {
        userService.deleteByIds(ids);
        return "success";
    }

    @GetMapping("/user/{id}")
    @ResponseBody
    public User getById(@PathVariable("id") Long id) {
        return userService.getById(id);
    }

    @GetMapping("/users")
    @ResponseBody
    public List<User> findAll() {
        return userService.findAll();
    }

//	@GetMapping("/users/page/{num}")
//	@ResponseBody
//	public List<User> list(@PathVariable("num") Integer pageNum) {
//		if (pageNum != null) {
//			PageHelper.startPage(pageNum, 2);
//		}
//		return userService.findAll();
//	}

    @GetMapping("/login")
    public String toLogin() {
        return "user/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(User user, HttpSession session) {
        User exitUser = userService.login(user);
        if (exitUser != null) {
            session.setAttribute("user", exitUser);
            return "redirect:/";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/user_info")
    @ResponseBody
    public User info(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user;
    }

//    //请求映射，跟浏览器中的URL请求资源对应就会调用对应的发放
//    @RequestMapping("/")
//    public String index() {
//        //前缀 + 视图名 + 后缀
//        //pages/index.html
//        return "index";
//    }
//    //请求映射，跟浏览器中的URL请求资源对应就会调用对应的发放
//    @RequestMapping("/host")
//    @ResponseBody
//    public String host() {
//        String host = null;
//        try {
//            host = InetAddress.getLocalHost().getHostName();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//        return host;
//    }
//
//    /***
//     * 验证手机号验证码
//     * @param user
//     * @return
//     */
//    @PostMapping("/verify")
//    @ResponseBody
//    public boolean verify(User user) {
//        boolean flag = userService.verify(user);
//        return flag;
//    }
//
//    /***
//     * 通过手机号获取验证码
//     * @param nationCode
//     * @param phoneNum
//     * @return
//     */
//    @PostMapping("/genCode")
//    @ResponseBody
//    public String genCode(String nationCode, String phoneNum) {
//        String msg = "true";
//        try {
//            //生成4位随机数 -> 调用短信接口发送验证码 -> 将手机号对应的验证码保存到redis中，并且设置这个key的有效时长
//            userService.genVerifyCode(nationCode, phoneNum);
//        } catch (Exception e) {
//            e.printStackTrace();
//            msg = "false";
//        }
//        return msg;
//    }
//    @GetMapping("/phoneNum/{openid}")
//    @ResponseBody
//    public User getPhoneNum(@PathVariable("openid") String openid) {
//        User user = userService.getUserByOpenid(openid);
//        return user;
//    }
//    @PostMapping("/recharge")
//    @ResponseBody
//    public boolean recharge(@RequestBody String params) {
//        boolean flag = true;
//        //System.out.println(params);
//        userService.recharge(params);
//        return flag;
//    }
//    @PostMapping("/deposit")
//    @ResponseBody
//    public String deposit(User user) {
//        userService.deposit(user);
//        return "success";
//    }
//    @PostMapping("/identify")
//    @ResponseBody
//    public String identify(User user) {
//        userService.identify(user);
//        return "success";
//    }
}
