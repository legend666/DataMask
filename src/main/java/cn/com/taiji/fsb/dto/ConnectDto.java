package cn.com.taiji.fsb.dto;

import cn.com.taiji.fsb.domain.Connect;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
public class ConnectDto implements Serializable {
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

    public ConnectDto() {

    }

    public ConnectDto(Connect u) {
        BeanUtils.copyProperties(u, this);
    }
}
