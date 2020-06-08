package cn.com.taiji.fsb.domain;


import cn.com.taiji.fsb.dto.ConnectDto;
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
@Table(name = "CONNECTIONINFO")
public class Connect implements Serializable{

    @Id
    private String connectionid;
    private String connectionname;
    private String serverip;
    private String dbname;
    private String dbuser;
    private String userpassword;
    private String dbtypeid;
    private String userid;
    private String createtime;
    private String additionalinfo;

    public Connect(){

    }

    public Connect(ConnectDto u){
        BeanUtils.copyProperties(u,this);
    }
}
