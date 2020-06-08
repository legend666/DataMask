package cn.com.taiji.fsb.web;

import cn.com.taiji.fsb.domain.User;
import cn.com.taiji.fsb.dto.DataConfigDto;
import cn.com.taiji.fsb.dto.TaskDto;
import cn.com.taiji.fsb.service.impl.DataConfigSerivceImpl;
import cn.com.taiji.fsb.service.impl.TaskSerivceImpl;
import net.minidev.json.JSONObject;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/restore")
public class RestoreController {
    @Autowired
    TaskSerivceImpl taskSerivceImpl;
    @Autowired
    DataConfigSerivceImpl dataConfigSerivceImpl;

    @RequestMapping(value = "/restore_saveTasks", method = RequestMethod.POST)
    @ResponseBody
    public String submitFromExcel(@RequestBody Map<String, String> data, Model model) {
        String path = data.get("pathId");
        String jsonStr = data.get("jsonStr");
        //TASKS表
        net.minidev.json.JSONObject jo = new JSONObject();
        String taskId = UUID.randomUUID().toString();
        String taskType = "1";    //还原任务
        String connectionId = UUID.randomUUID().toString();    //DataConfigs的外键
        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = user.getUserid();   //SystemUsers的外键
        String[] aa = path.split("\\\\");
        String taskName = user.getLoginname() + "还原平面文件"+aa[aa.length-1]+"数据";
        String createTime = data.get("CreateTime");
        String taskStatus = "0";    //未执行
        TaskDto dto = new TaskDto();
        dto.setTaskid(taskId);
        dto.setTaskname(taskName);
        dto.setTasktype(taskType);
        dto.setConnectionid(connectionId);
        dto.setUserid(userId);
        dto.setCreatetime(createTime);
        dto.setTaskstatus(taskStatus);

        try {
            taskSerivceImpl.save(dto);
            jo.put("TaskId", taskId);
            jo.put("TaskName", dto.getTaskname());
            jo.put("TaskType", dto.getTasktype());
            jo.put("ConnectionId", dto.getConnectionid());
            jo.put("UserId", dto.getUserid());
            jo.put("CreateTime", dto.getCreatetime());
            jo.put("TaskStatus", dto.getTaskstatus());
            jo.put("Result", 1);
        } catch (Exception exe) {
            jo.put("Result", 0);
            jo.put("ErrorMsg", exe.getMessage());
        }

        //DataConfigs表
        String configId = connectionId;
        String configName = taskName+"配置";
        String configType = "1";    //还原任务配置
        String configContent = jsonStr;    //待还原内容的json字符串
        String configuserId = userId;
        String configcreateTime = createTime;
        String configConnectionId = connectionId;
        DataConfigDto dto2 = new DataConfigDto();
        dto2.setConfigid(configId);
        dto2.setConfigname(configName);
        dto2.setConfigtype(configType);
        dto2.setConfigcontent(configContent);
        dto2.setUserid(configuserId);
        dto2.setCreatetime(configcreateTime);
        dto2.setConnectionid(configConnectionId);

        try {
            dataConfigSerivceImpl.save(dto2);
            jo.put("Result2", 1);
        } catch (Exception exe) {
            jo.put("Result2", 0);
            jo.put("ErrorMsg", exe.getMessage());
        }

        return configId;
    }

    @RequestMapping(value = "/restore_submitTasks", method = RequestMethod.POST)
    @ResponseBody
    public String restore_submit(@RequestBody Map<String, String> data) throws Exception {
        //轮询扫表
        new TaskManage().timedTask(taskSerivceImpl, dataConfigSerivceImpl);
        return null;
    }

    @RequestMapping(value = "/restore_getRestoreContents", method = RequestMethod.POST)
    @ResponseBody
    public JSONArray restore_getContents(@RequestBody Map<String, String> data) throws Exception {
        //获取DataConfig表数据
        String taskId = data.get("tableId");
        String contents = dataConfigSerivceImpl.getDataConfigById(taskId).getConfigcontent();
        //将字符串转jasonArray
        JSONArray jsonArray = JSONArray.fromObject(contents);
        return jsonArray;
    }
}
