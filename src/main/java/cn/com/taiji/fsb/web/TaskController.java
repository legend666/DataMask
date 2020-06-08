package cn.com.taiji.fsb.web;

import cn.com.taiji.fsb.domain.Task;
import cn.com.taiji.fsb.service.impl.TaskSerivceImpl;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/task")
public class TaskController {
    @Autowired
    TaskSerivceImpl taskSerivceImpl;

    @RequestMapping(value = "/task_manage", method = RequestMethod.GET)
    @ResponseBody
    public String getExcelData(HttpServletRequest request, HttpServletResponse response) {
        //显示TASKS表
        List<Task> lsTasks = taskSerivceImpl.getAllTask();
        JSONObject jo = new JSONObject();
        jo.put("page", 1);
        JSONArray joRows = new JSONArray();
        jo.put("rows", joRows);
        jo.put("total", 1);
        jo.put("records", lsTasks.size());
        lsTasks.forEach(u -> {
            JSONObject joUser = new JSONObject();
            if(u.getTasktype().equals("1")){ joUser.put("type", "还原任务");}
            else{joUser.put("type", "脱敏任务");}
            joUser.put("name", u.getTaskname());
            if(u.getTaskstatus().equals("0")){joUser.put("status","未执行");}
            else{
                if (u.getTaskstatus().equals("1")) {joUser.put("status","执行中");}
                else{
                    if(u.getTaskstatus().equals("2")){joUser.put("status","成功");}
                    else{joUser.put("status","失败");}
                }
            }
            joUser.put("time", u.getCreatetime());
            joUser.put("messaage", "");
            joRows.add(joUser);
        });
        return jo.toString();
    }

}
