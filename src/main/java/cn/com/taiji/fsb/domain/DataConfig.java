package cn.com.taiji.fsb.domain;

import cn.com.taiji.fsb.dto.DataConfigDto;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @description : 连接实体
 */
@Data
@Entity
@Table(name = "DATACONFIGS")
public class DataConfig implements Serializable{

    @Id
    private String configid;
    private String configname;
    private String configtype;
    private String configcontent;
    private String userid;
    private String createtime;
    private String connectionid;

    public DataConfig(){

    }

    public DataConfig(DataConfigDto u){
        BeanUtils.copyProperties(u,this);
    }
}
