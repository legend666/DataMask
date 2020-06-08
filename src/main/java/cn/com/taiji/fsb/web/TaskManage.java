package cn.com.taiji.fsb.web;

import cn.com.taiji.fsb.domain.Connect;
import cn.com.taiji.fsb.domain.DataConfig;
import cn.com.taiji.fsb.domain.Task;
import cn.com.taiji.fsb.dto.DataConfigDto;
import cn.com.taiji.fsb.dto.TaskDto;
import cn.com.taiji.fsb.service.impl.ConnectSerivceImpl;
import cn.com.taiji.fsb.service.impl.DataConfigSerivceImpl;
import cn.com.taiji.fsb.service.impl.TaskSerivceImpl;
import com.idealista.fpe.decrypt.BackFromExcel;
import com.idealista.fpe.encrypt.EncryptOnSQLServer;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

//任务管理类，轮询TASKS表
public class TaskManage {

    public void timedTask(TaskSerivceImpl taskserviceimpl, DataConfigSerivceImpl dataconfigserviceimpl){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("进行还原任务");
                List<Task> lsTasks = taskserviceimpl.getAllTask();
                for(Task task : lsTasks){
                    if(task.getTaskstatus().equals("0") && task.getTasktype().equals("1")){
                        //修改TASKS表状态为：执行中
                        String status = "1";
                        TaskDto dto = new TaskDto();
                        dto.setTaskid(task.getTaskid());
                        dto.setTaskname(task.getTaskname());
                        dto.setTasktype(task.getTasktype());
                        dto.setConnectionid(task.getConnectionid());
                        dto.setUserid(task.getUserid());
                        dto.setCreatetime(task.getCreatetime());
                        dto.setTaskstatus(status);
                        taskserviceimpl.save(dto);

                        String foreignkey = task.getConnectionid();
                        DataConfig dataConfig = dataconfigserviceimpl.getDataConfigById(foreignkey);
                        String content = dataConfig.getConfigcontent();
                        JSONArray result = new BackFromExcel().back(content);
                        if(result.isEmpty()){
                            status = "3";    //任务状态：失败
                        }else{
                            status = "2";    //任务状态：成功
                        }

                        //修改TASKS表状态为：成功或失败
                        TaskDto dto2 = new TaskDto();
                        dto2.setTaskid(task.getTaskid());
                        dto2.setTaskname(task.getTaskname());
                        dto2.setTasktype(task.getTasktype());
                        dto2.setConnectionid(task.getConnectionid());
                        dto2.setUserid(task.getUserid());
                        dto2.setCreatetime(task.getCreatetime());
                        dto2.setTaskstatus(status);
                        taskserviceimpl.save(dto2);

                        //把还原结果保存到dataconfig表中这样导出的时候可以从数据库中取
                        DataConfigDto dto3 = new DataConfigDto();
                        dto3.setConfigid(dataConfig.getConfigid());
                        dto3.setConfigname(dataConfig.getConfigname());
                        dto3.setConfigtype(dataConfig.getConfigtype());
                        dto3.setConfigcontent(result.toString());
                        dto3.setUserid(dataConfig.getUserid());
                        dto3.setCreatetime(dataConfig.getCreatetime());
                        dto3.setConnectionid(dataConfig.getConnectionid());
                        dataconfigserviceimpl.save(dto3);
                    }
                }
            }
        };
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(runnable, 1, 1, TimeUnit.SECONDS);
    }

    public void timedTask(TaskSerivceImpl taskserviceimpl, DataConfigSerivceImpl dataconfigserviceimpl, ConnectSerivceImpl connectSerivceImpl){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("进行脱敏任务");
                List<Task> lsTasks = taskserviceimpl.getAllTask();
                for(Task task : lsTasks){
                    if(task.getTaskstatus().equals("0") && task.getTasktype().equals("0")){
                        //修改TASKS表状态为：执行中
                        String status = "1";
                        TaskDto dto = new TaskDto();
                        dto.setTaskid(task.getTaskid());
                        dto.setTaskname(task.getTaskname());
                        dto.setTasktype(task.getTasktype());
                        dto.setConnectionid(task.getConnectionid());
                        dto.setUserid(task.getUserid());
                        dto.setCreatetime(task.getCreatetime());
                        dto.setTaskstatus(status);
                        taskserviceimpl.save(dto);

                        String foreignkey = task.getConnectionid();
                        DataConfig dataConfig = dataconfigserviceimpl.getDataConfigById(foreignkey);
                        String content = dataConfig.getConfigcontent();
                        JSONObject js = JSONObject.fromObject(content);
                        String maskingData = js.get("maskingData").toString();
                        String ConnectionName = js.get("ConnectionName").toString();
                        //脱敏
                        boolean result = mask(maskingData,ConnectionName,connectSerivceImpl);

                        if(result){
                            status = "2";    //成功
                        }else{
                            status = "3";    //失败
                        }

                        //修改TASKS表状态为：成功或失败
                        TaskDto dto2 = new TaskDto();
                        dto2.setTaskid(task.getTaskid());
                        dto2.setTaskname(task.getTaskname());
                        dto2.setTasktype(task.getTasktype());
                        dto2.setConnectionid(task.getConnectionid());
                        dto2.setUserid(task.getUserid());
                        dto2.setCreatetime(task.getCreatetime());
                        dto2.setTaskstatus(status);
                        taskserviceimpl.save(dto2);

                    }
                }
            }
        };
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(runnable, 1, 1, TimeUnit.SECONDS);
    }

    public boolean mask(String maskingData, String ConnectionName, ConnectSerivceImpl connectSerivceImpl){
        List<Connect> lsConnects = connectSerivceImpl.getAllConnect();
        Map<String, String> conmap = new HashMap<>();
//        {连接名：id},找到对应的id，然后取到整条值
        for (Connect u : lsConnects) {
            conmap.put(u.getConnectionname(), u.getConnectionid());
        }
        String connectionId = conmap.get(ConnectionName);
        Connect coninfo = connectSerivceImpl.getConnectById(connectionId);
        String Data = maskingData.replace("\"", "");
        String[] str = Data.split(";");
        for (int i = 0; i < str.length; i++) {
            System.out.println("--" + str[i]);
            String[] DataStr = str[i].split(",");
            String table = null;
            String maskingColumn = null;
            String maskingRules = null;
            for (int j = 0; j < DataStr.length; j++) {
                System.out.println(DataStr[j]);
                if (j == 0) {
                    table = DataStr[j];
                } else if (j == 1) {
                    maskingColumn = DataStr[j];
                } else if (j == 2) {
                    maskingRules = DataStr[j];
                }
//                System.out.println("=="+DataStr[j]);
            }
            String finalMaskingColumn = maskingColumn;
            String finalMaskingRules = maskingRules;
            Map<String, String> hashmap = new HashMap<String, String>() {
                {
                    put(finalMaskingColumn, finalMaskingRules);
                }
            };
            System.out.println(table);
            System.out.println(hashmap);
            String ip = coninfo.getServerip();
            String databaseName = coninfo.getDbname();
            String userName = coninfo.getDbuser();
            String password = coninfo.getUserpassword();
            EncryptOnSQLServer encrypter = new EncryptOnSQLServer(ip, databaseName, userName, password);
            encrypter.encrypt(table, hashmap);    //参数为数据库表名和规则
        }
        return true;
    }
}
