package cn.com.taiji.fsb.web;

import cn.com.taiji.fsb.domain.Connect;
import cn.com.taiji.fsb.domain.User;
import cn.com.taiji.fsb.dto.ConnectDto;
import cn.com.taiji.fsb.service.impl.ConnectSerivceImpl;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/connect")
public class ConnectController {
    @Autowired
    ConnectSerivceImpl connectSerivceImpl;

    @RequestMapping(value = "/getAllConnect", method = RequestMethod.GET)
    @ResponseBody
    public String getAllConnect(HttpServletRequest request, HttpServletResponse response) {
        String sort = request.getParameter("sidx");
        String sord = request.getParameter("sord");
        if (sort.isEmpty()) {
            sort = "1";
        }
        List<Connect> lsConnects = connectSerivceImpl.getAllConnect();
        JSONObject jo = new JSONObject();
        jo.put("page", 1);
        JSONArray joRows = new JSONArray();
        jo.put("rows", joRows);
        jo.put("total", 1);
        jo.put("records", lsConnects.size());
        lsConnects.forEach(u -> {
            JSONObject joConnect = new JSONObject();
            joConnect.put("ConnectionId",u.getConnectionid());
            joConnect.put("ConnectionName", u.getConnectionname());
            joConnect.put("ServerIp", u.getServerip());
            joConnect.put("DbName", u.getDbname());
            joConnect.put("DbTypeId", u.getDbtypeid());
            joConnect.put("DbUser", u.getDbuser());
            joConnect.put("UserPassword",u.getUserpassword());
            joConnect.put("CreateTime", u.getCreatetime());
            joConnect.put("UserId",u.getUserid());
            joConnect.put("AdditionalInfo",u.getAdditionalinfo());
            joRows.add(joConnect);
        });
        return jo.toString();
    }

    @RequestMapping(value = "/createConnect", method = RequestMethod.POST)
    @ResponseBody
    public String createConnect(@RequestBody Map<String, String> data) throws Exception {
        JSONObject joConnect = new JSONObject();
        String ConnectionId = UUID.randomUUID().toString();
        String createTime = data.get("CreateTime");
        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ConnectDto dto = new ConnectDto();
        dto.setCreatetime(createTime);
        dto.setConnectionid(ConnectionId);
        dto.setConnectionname(data.get("ConnectionName"));
        dto.setDbtypeid(data.get("DbTypeId"));
        dto.setServerip(data.get("ServerIp"));
        dto.setDbname(data.get("DbName"));
        dto.setDbuser(data.get("DbUser"));
        dto.setUserpassword(data.get("UserPassword"));
        dto.setUserid(user.getLoginname());
        dto.setAdditionalinfo(data.get("AdditionalInfo"));
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        Date d = sdf.parse(createTime);
//        dto.setCreatetime(d);

        try {
            connectSerivceImpl.save(dto);
            joConnect.put("ConnectionId",dto.getConnectionid());
            joConnect.put("ConnectionName", dto.getConnectionname());
            joConnect.put("ServerIp", dto.getServerip());
            joConnect.put("DbName", dto.getDbname());
            joConnect.put("DbTypeId", dto.getDbtypeid());
            joConnect.put("DbUser", dto.getDbuser());
            joConnect.put("UserPassword",dto.getUserpassword());
            joConnect.put("CreateTime", dto.getCreatetime());
            joConnect.put("UserId",dto.getUserid());
            joConnect.put("AdditionalInfo",dto.getAdditionalinfo());
            joConnect.put("Result", 1);
        } catch (Exception exe) {
            joConnect.put("Result", 0);
            joConnect.put("ErrorMsg", exe.getMessage());
        }
        return joConnect.toJSONString();
    }

    @RequestMapping(value = "/editConnect", method = RequestMethod.POST)
    @ResponseBody
    public String editConnect(@RequestBody Map<String, String> data) {
        System.out.println(data);
        Connect con = connectSerivceImpl.getConnectById(data.get("conid"));
        ConnectDto condto = new ConnectDto(con);
        System.out.println(condto);
        condto.setConnectionname(data.get("connectionName"));
        condto.setDbtypeid(data.get("DbTypeId"));
        condto.setServerip(data.get("ServerIp"));
        condto.setDbname(data.get("DbName"));
        condto.setDbuser(data.get("DbUser"));
        condto.setUserpassword(data.get("UserPassword"));
        condto.setAdditionalinfo(data.get("AdditionalInfo"));
        try {
            connectSerivceImpl.save(condto);
            return "1";
        } catch (Exception exe) {
            return "0";
        }
    }

    @RequestMapping(value = "/deleteConnectByConnectId", method = RequestMethod.POST)
    @ResponseBody
    public String deleteConnectByConnectId(@RequestBody Map<String, String> data) {
        String ConId = data.get("ConnectionId");
        connectSerivceImpl.deleteConnectById(ConId);
        Connect con = connectSerivceImpl.getConnectById(ConId);
        if (con == null) {
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
