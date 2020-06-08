package cn.com.taiji.fsb.web;

import cn.com.taiji.fsb.dto.UserDto;
import cn.com.taiji.fsb.service.impl.UserSerivceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.UUID;

@Controller
@RequestMapping("/menuinfo")
public class MenuController {
    @Autowired
    UserSerivceImpl userSerivceImpl;

    @RequestMapping("/menuinfo_restore")
    public String menuinfoShow() {
        return "inspection/menuinfo_restore";//数据还原页
    }

    @RequestMapping("/menuinfo_connect")
    public String menuconnectShow() {
        return "inspection/menuinfo_connect";//数据连接页
    }

    @RequestMapping("/menuinfo_task")
    public String menutaskShow() {
        return "inspection/menuinfo_task";//数据连接页
    }

    @RequestMapping("/menuinfo_set")
    public String menuinfosetShow() {
        return "inspection/menuinfo_set";//系统设置页
    }

    @RequestMapping("/menuinfo_help")
    public String menuinfohelpShow() {
        return "inspection/menuinfo_help";//系统帮助页
    }

    @RequestMapping("/menuinfo_user")
    public String menuUserManagement(){
        return "inspection/user";
    }


    @RequestMapping("/userInfo_insert")
    public String userInsert(Model model, @ModelAttribute("dto") UserDto dto) {
        String uuid = UUID.randomUUID().toString();    //获取UUID并转化为String对象
        uuid = uuid.replace("-", "");    //因为UUID本身为32位只是生成时多了“-”，所以将它们去点就可
        dto.setUserid(uuid);
        Date date = new Date();
        dto.setCreatetime(date.toString());
        String password = dto.getPassword();
        MessageDigestPasswordEncoder md = new MessageDigestPasswordEncoder("md5");
        dto.setPassword(md.encodePassword(password, null));
        userSerivceImpl.save(dto);//
        return "inspection/menuinfo_set";//系统帮助页
    }

}
