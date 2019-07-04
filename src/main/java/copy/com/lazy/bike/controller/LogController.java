package copy.com.lazy.bike.controller;

import copy.com.lazy.bike.servce.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LogController {

    @Autowired
    private LogService logService;

    @PostMapping("/log/ready")
    @ResponseBody
    public String ready(@RequestBody String log) {
        logService.save(log);
        return "success";
    }
}
