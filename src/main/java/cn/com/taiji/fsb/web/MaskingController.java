package cn.com.taiji.fsb.web;

import cn.com.taiji.fsb.domain.Connect;
import cn.com.taiji.fsb.domain.User;
import cn.com.taiji.fsb.dto.DataConfigDto;
import cn.com.taiji.fsb.dto.TaskDto;
import cn.com.taiji.fsb.service.impl.ConnectSerivceImpl;
import cn.com.taiji.fsb.service.impl.DataConfigSerivceImpl;
import cn.com.taiji.fsb.service.impl.TaskSerivceImpl;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/Masking")
public class MaskingController {
    @Autowired
    ConnectSerivceImpl connectSerivceImpl;
    @Autowired
    TaskSerivceImpl taskSerivceImpl;
    @Autowired
    DataConfigSerivceImpl dataConfigSerivceImpl;

    @RequestMapping(value = "/maskingData", method = RequestMethod.POST)
    @ResponseBody
    public void maskingData(String maskingData, String ConnectionName) {
        List<Connect> lsConnects = connectSerivceImpl.getAllConnect();
        Map<String, String> conmap = new HashMap<>();
//        {连接名：id},找到对应的id，然后取到整条值
        for (Connect u : lsConnects) {
            conmap.put(u.getConnectionname(), u.getConnectionid());
        }
        String connectionId = conmap.get(ConnectionName);
        Connect coninfo = connectSerivceImpl.getConnectById(connectionId);

        String taskId = UUID.randomUUID().toString();
        String taskType = "0";    //脱敏任务
        String configId = UUID.randomUUID().toString();    //DataConfigs的外键
        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = user.getUserid();   //SystemUsers的外键
        String taskName = user.getLoginname() + "脱敏" + coninfo.getConnectionname()+"数据";
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = sdf.format( new Date());    //获取当前时间
        String taskStatus = "0";    //未执行
        TaskDto dto = new TaskDto();
        dto.setTaskid(taskId);
        dto.setTaskname(taskName);
        dto.setTasktype(taskType);
        dto.setConnectionid(configId);
        dto.setUserid(userId);
        dto.setCreatetime(createTime);
        dto.setTaskstatus(taskStatus);
        taskSerivceImpl.save(dto);

        //DataConfigs表
        String configName = taskName+"配置";
        String configType = "0";    //脱敏任务配置
        net.minidev.json.JSONObject jo = new JSONObject();
        jo.put("maskingData",maskingData);
        jo.put("ConnectionName",ConnectionName);
        String configContent = jo.toString();    //待脱敏内容字符串和数据连接字符串，以$符号分割
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

        dataConfigSerivceImpl.save(dto2);
    }

    @RequestMapping(value = "/MaskingSubmitTasks", method = RequestMethod.POST)
    @ResponseBody
    public String restore_submit(@RequestBody Map<String, String> data) throws Exception {
        //轮询扫表
        new TaskManage().timedTask(taskSerivceImpl, dataConfigSerivceImpl, connectSerivceImpl);
        return null;
    }
}
