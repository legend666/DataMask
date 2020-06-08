package cn.com.taiji.fsb.dto;

import cn.com.taiji.fsb.domain.Task;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
public class TaskDto implements Serializable {
    private String taskid;
    private String taskname;
    private String tasktype;
    private String connectionid;
    private String userid;
    private String createtime;
    private String taskstatus;

    public TaskDto() {

    }

    public TaskDto(Task u) {
        BeanUtils.copyProperties(u, this);
    }
}
