package cn.com.taiji.fsb.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/indexinfo")
public class IndexController {

    @Autowired
    protected AuthenticationManager authenticationManager;

    @RequestMapping("/index")
    public String index() {
        return "inspection/main";
    }
}