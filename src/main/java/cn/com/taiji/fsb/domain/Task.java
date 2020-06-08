package cn.com.taiji.fsb.domain;


import cn.com.taiji.fsb.dto.TaskDto;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @description : 任务实体
 */
@Data
@Entity
@Table(name = "TASKS")
public class Task implements Serializable{
    @Id
    private String taskid;
    private String taskname;
    private String tasktype;
    private String connectionid;
    private String userid;
    private String createtime;
    private String taskstatus;

    public Task(){

    }

    public Task(TaskDto u){
        BeanUtils.copyProperties(u,this);
    }
}
