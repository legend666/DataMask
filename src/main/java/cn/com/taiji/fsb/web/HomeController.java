package cn.com.taiji.fsb.web;

import cn.com.taiji.fsb.annotation.HandleLog;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description : @see HomeController
 */

@Controller
public class HomeController {


    @HandleLog(types = "登录模块" ,describe = "用户登录")
    @RequestMapping(value = "/loginSuccess")
    public String loginSuccess(){
      // User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "frames/index";
    }

    @RequestMapping("/main")
    public String main(){
        return "inspection/main";
    }
}
