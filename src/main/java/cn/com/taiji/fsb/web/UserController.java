package cn.com.taiji.fsb.web;

import cn.com.taiji.fsb.domain.User;
import cn.com.taiji.fsb.dto.UserDto;
import cn.com.taiji.fsb.service.impl.UserSerivceImpl;
import cn.com.taiji.fsb.util.FsbUtil;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserSerivceImpl userSerivceImpl;

    @RequestMapping(value = "/user_getAllUsers", method = RequestMethod.GET)
    @ResponseBody
    public String getAllUsers(HttpServletRequest request, HttpServletResponse response) {
        String sort = request.getParameter("sidx");
        String sord = request.getParameter("sord");
        if (sort.isEmpty()) {
            sort = "1";
        }
        List<User> lsUsers = userSerivceImpl.getAllUsers();
        JSONObject jo = new JSONObject();
        jo.put("page", 1);
        JSONArray joRows = new JSONArray();
        jo.put("rows", joRows);
        jo.put("total", 1);
        jo.put("records", lsUsers.size());
        lsUsers.forEach(u -> {
            JSONObject joUser = new JSONObject();
            joUser.put("UserId", u.getUserid());
            joUser.put("UserName", u.getUsername());
            joUser.put("LoginName", u.getLoginname());
            joUser.put("UserRole", u.getUserrole());
            joUser.put("Password", u.getPassword());
            joUser.put("CreateTime", u.getCreatetime());
            joRows.add(joUser);
        });
//        String aaa = jo.toJSONString();
//        String bbb = jo.toString();
        return jo.toString();
    }

    @RequestMapping(value = "/user_createUser", method = RequestMethod.POST)
    @ResponseBody
    public String user_createUser(@RequestBody Map<String, String> data) throws Exception {
        JSONObject jo = new JSONObject();
        String userId = UUID.randomUUID().toString();
        String password = FsbUtil.md5(data.get("Password"));
        String createTime = data.get("CreateTime");
        UserDto dto = new UserDto();
        dto.setUserid(userId);
        dto.setUsername(data.get("UserName"));
        dto.setLoginname(data.get("LoginName"));
        dto.setUserrole(data.get("UserRole"));
        dto.setPassword(password);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        Date d = sdf.parse(createTime);
//        dto.setCreatetime(d);
        dto.setCreatetime(createTime);
        try {
            userSerivceImpl.save(dto);
            jo.put("UserId", userId);
            jo.put("UserName", dto.getUsername());
            jo.put("LoginName", dto.getLoginname());
            jo.put("UserRole", dto.getUserrole());
            jo.put("Password", password);
            jo.put("CreateTime", createTime);
            jo.put("Result", 1);
        } catch (Exception exe) {
            jo.put("Result", 0);
            jo.put("ErrorMsg", exe.getMessage());
        }
        return jo.toJSONString();
    }

    @RequestMapping(value = "/user_editUser", method = RequestMethod.POST)
    @ResponseBody
    public String editUser(@RequestBody Map<String, String> data) {
        User user = userSerivceImpl.getUserById(data.get("UserId"));
        UserDto dto = new UserDto(user);
        dto.setUsername(data.get("UserName"));
        dto.setUserrole(data.get("UserRole"));
        try {
            userSerivceImpl.save(dto);
            return "1";
        } catch (Exception exe) {
            return "0";
        }
    }

    @RequestMapping(value = "/user_deleteUserByUserId", method = RequestMethod.POST)
    @ResponseBody
    public String deleteUserByUserId(@RequestBody Map<String, String> data) {
        String uid = data.get("UserId");
        userSerivceImpl.deleteUserByUserId(uid);
        User u = userSerivceImpl.getUserById(data.get("UserId"));
        if (u == null) {
            return "1";
        }
        return "0";
    }

    @RequestMapping(value = "/userInfo_test", method = RequestMethod.GET)
    public void testGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        response.getWriter().print(name);
    }
}
